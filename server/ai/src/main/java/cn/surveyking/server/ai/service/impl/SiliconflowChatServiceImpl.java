package cn.surveyking.server.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import cn.surveyking.server.ai.domain.ChatRequest;
import cn.surveyking.server.ai.domain.ConversationRequest;
import cn.surveyking.server.ai.domain.ConversationResponse;
import cn.surveyking.server.ai.domain.ModelType;
import cn.surveyking.server.ai.domain.StreamResponseEvent;
import cn.surveyking.server.ai.domain.AiMessage;
import cn.surveyking.server.ai.domain.EventTypeEnum;
import cn.surveyking.server.ai.service.AiChatService;
import cn.surveyking.server.service.SystemService;
import cn.surveyking.server.domain.dto.SystemInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import reactor.util.retry.Retry;

/**
 * SiliconFlow 聊天服务实现类
 * 支持通过SiliconFlow平台调用多种AI模型，包括DeepSeek、Qwen、Llama等
 * 配置从SystemService获取而不是配置文件
 *
 * @author zzr
 */
@Slf4j
@Service
public class SiliconflowChatServiceImpl implements AiChatService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final SystemService systemService;

    // SiliconFlow API基础URL
    private static final String SILICONFLOW_BASE_URL = "https://api.siliconflow.cn";

    public SiliconflowChatServiceImpl(WebClient webClient, ObjectMapper objectMapper, SystemService systemService) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.systemService = systemService;
    }

    /**
     * 获取AI配置
     */
    private SystemInfo.AiSetting getAiSetting() {
        SystemInfo.AiSetting aiSetting = systemService.getSystemAiSetting();
        if (aiSetting == null) {
            log.warn("AI setting is null, creating default setting");
            aiSetting = new SystemInfo.AiSetting();
            aiSetting.setEnabled(false);
        }
        return aiSetting;
    }

    @Override
    public boolean isEnabled() {
        SystemInfo.AiSetting aiSetting = getAiSetting();
        return aiSetting.getEnabled() != null && aiSetting.getEnabled();
    }

    @Override
    public List<ModelType> getSupportedModels() {
        SystemInfo.AiSetting aiSetting = getAiSetting();
        if (aiSetting.getModels() == null || aiSetting.getModels().isEmpty()) {
            return Collections.emptyList();
        }

        // 将字符串列表转换为ModelType列表
        return aiSetting.getModels().stream()
                .map(model -> new ModelType(model, model, "AI模型: " + model))
                .collect(Collectors.toList());
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest conversationRequest) {
        // 创建一个新的 ConversationResponse 对象
        ConversationResponse response = new ConversationResponse();
        response.setId(UUID.randomUUID().toString().replace("-", ""));
        response.setCreatedAt(System.currentTimeMillis());

        // 设置模型类型和ID
        Map<String, String> metaData = new HashMap<>();
        metaData.put("modelType", "siliconflow");

        // 从请求中获取模型，如果没有则使用第一个可用模型
        String modelId = null;
        if (conversationRequest != null && conversationRequest.getModelType() != null) {
            modelId = conversationRequest.getModelType();
        } else {
            List<ModelType> models = getSupportedModels();
            if (!models.isEmpty()) {
                modelId = models.get(0).getValue();
            }
        }
        metaData.put("modelId", modelId != null ? modelId : "deepseek-chat");
        response.setMetaData(metaData);

        return response;
    }

    @Override
    public Flux<StreamResponseEvent> createChatStream(ChatRequest chatRequest, String conversationId, String model,
            Consumer<AiMessage> consumer) {

        // 检查AI是否启用
        if (!isEnabled()) {
            return Flux.just(new StreamResponseEvent(EventTypeEnum.error, "AI功能未启用"));
        }

        SystemInfo.AiSetting aiSetting = getAiSetting();
        if (StringUtils.isEmpty(aiSetting.getToken())) {
            return Flux.just(new StreamResponseEvent(EventTypeEnum.error, "AI Token未配置"));
        }

        // 使用传入的model参数，如果为空则使用第一个可用模型
        String selectedModel = model;
        if (StringUtils.isEmpty(selectedModel)) {
            List<ModelType> models = getSupportedModels();
            if (!models.isEmpty()) {
                selectedModel = models.get(0).getValue();
            } else {
                return Flux.just(new StreamResponseEvent(EventTypeEnum.error, "没有可用的AI模型"));
            }
        }

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", selectedModel);
        requestBody.put("stream", true);
        requestBody.put("max_tokens", 4096);
        requestBody.put("temperature", 0.7);
        requestBody.put("top_p", 0.7);

        // 构建消息列表
        List<Map<String, String>> messages = new ArrayList<>();

        // 添加系统 prompt 消息
        AiMessage prompt = getPrompt("siliconflow", selectedModel);
        if (prompt != null && prompt.getContent() != null) {
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", prompt.getRole());
            systemMessage.put("content", prompt.getContent());
            messages.add(systemMessage);
        }

        // 添加历史消息（最多10条）
        List<ChatRequest.EnterMessage> historyMessages = chatRequest.getAdditionalMessages();
        if (historyMessages != null) {
            int startIndex = Math.max(0, historyMessages.size() - 10);
            for (int i = startIndex; i < historyMessages.size(); i++) {
                ChatRequest.EnterMessage msg = historyMessages.get(i);
                Map<String, String> historyMessage = new HashMap<>();
                historyMessage.put("role", msg.getRole() != null ? msg.getRole() : "user");
                historyMessage.put("content", msg.getContent());
                messages.add(historyMessage);
            }
        }

        requestBody.put("messages", messages);

        // 发送请求
        List<String> contentList = new ArrayList<>();
        return webClient.post()
                .uri(SILICONFLOW_BASE_URL + "/v1/chat/completions")
                .header("Authorization", "Bearer " + aiSetting.getToken())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .timeout(Duration.ofMinutes(5))
                .mapNotNull(original -> {
                    log.info("AI response: {}", original);
                    if ("[DONE]".equals(original)) {
                        // 将所有的 content 保存到 AiMessage
                        consumer.accept(new AiMessage(conversationId, "assistant", String.join("", contentList)));
                        return new StreamResponseEvent(EventTypeEnum.done, "");
                    }
                    try {
                        JsonNode jsonObject = objectMapper.readTree(original);
                        JsonNode delta = jsonObject.path("choices")
                                .get(0)
                                .path("delta");

                        // 处理DeepSeek的推理内容（如果存在）
                        String reasoningData = delta.path("reasoning_content").asText(null);
                        if (StringUtils.hasText(reasoningData)) {
                            return new StreamResponseEvent(EventTypeEnum.in_progress, "", reasoningData);
                        }

                        String content = delta.path("content").asText(null);
                        if (content != null) {
                            contentList.add(content);
                            return new StreamResponseEvent(EventTypeEnum.in_progress, content);
                        }
                        return null;
                    } catch (Exception e) {
                        log.error("Failed to parse response: {}", original, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException wre = (WebClientResponseException) e;
                        log.error("HTTP error: {} - {}", wre.getStatusCode(), wre.getResponseBodyAsString());
                        return Flux.just(
                                new StreamResponseEvent(EventTypeEnum.error, "HTTP error: " + wre.getStatusCode()));
                    } else {
                        log.error("Network error", e);
                        return Flux
                                .just(new StreamResponseEvent(EventTypeEnum.error, "Network error: " + e.getMessage()));
                    }
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .concatWith(Flux.just(new StreamResponseEvent(EventTypeEnum.done, "")));
    }

    @Override
    @SneakyThrows
    public AiMessage getPrompt(String modelType, String modelId) {
        SystemInfo.AiSetting aiSetting = getAiSetting();

        // 创建一个新的消息对象
        AiMessage message = new AiMessage();
        message.setRole("system");

        // 优先使用系统配置的提示词，如果没有则使用默认提示词
        String prompt = aiSetting.getPrompt();
        if (StringUtils.isEmpty(prompt)) {
            ClassPathResource resource = new ClassPathResource("prompt/siliconflow.md");
            prompt = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        }
        message.setContent(prompt);
        return message;
    }

}