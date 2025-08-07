package cn.surveyking.server.ai.service.impl;

import cn.surveyking.server.ai.domain.ChatRequest;
import cn.surveyking.server.ai.domain.ConversationRequest;
import cn.surveyking.server.ai.domain.ConversationResponse;
import cn.surveyking.server.ai.domain.ModelType;
import cn.surveyking.server.ai.domain.StreamResponseEvent;
import cn.surveyking.server.ai.domain.AiMessage;
import cn.surveyking.server.ai.service.AiChatService;
import cn.surveyking.server.ai.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 聊天服务实现类
 *
 * @author zzr
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private SiliconflowChatServiceImpl siliconflowChatService;

    @Override
    public List<ModelType> getAllModelTypes() {
        // 检查AI是否启用
        if (!siliconflowChatService.isEnabled()) {
            log.warn("AI功能未启用，返回空模型列表");
            return Collections.emptyList();
        }

        // 返回 SiliconFlow 支持的模型
        return siliconflowChatService.getSupportedModels();
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest conversationRequest, String model) {
        // 检查AI是否启用
        if (!siliconflowChatService.isEnabled()) {
            log.warn("AI功能未启用，无法创建对话");
            throw new IllegalStateException("AI功能未启用");
        }

        // 使用 SiliconFlow 服务创建对话
        return siliconflowChatService.createConversation(conversationRequest);
    }

    @Override
    public Flux<StreamResponseEvent> createChatStream(ChatRequest chatRequest, String conversationId, String model) {
        // 使用空的 consumer，因为当前系统没有实现消息持久化
        Consumer<AiMessage> emptyConsumer = message -> {
            // 这里可以添加消息保存逻辑，如果需要的话
            log.debug("Received AI message: {}", message.getContent());
        };

        return siliconflowChatService.createChatStream(chatRequest, conversationId, model, emptyConsumer);
    }
}