package cn.surveyking.server.ai.controller;

import cn.surveyking.server.ai.domain.ChatRequest;
import cn.surveyking.server.ai.domain.ConversationRequest;
import cn.surveyking.server.ai.domain.ConversationResponse;
import cn.surveyking.server.ai.domain.ModelType;
import cn.surveyking.server.ai.domain.StreamResponseEvent;
import cn.surveyking.server.ai.service.ChatService;
import cn.surveyking.server.ai.service.ConversationCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/ai/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ConversationCacheService conversationCacheService;

    @GetMapping("/models")
    public List<ModelType> getModels() {
        return chatService.getAllModelTypes();
    }

    @PostMapping("/create-conversation")
    public ConversationResponse createConversation(
            @RequestBody(required = false) ConversationRequest conversationRequest,
            @RequestParam(required = false) String model) {
        ConversationResponse response = chatService.createConversation(conversationRequest, model);

        // 创建对话缓存
        conversationCacheService.createConversation(response.getId());

        return response;
    }

    /**
     * 关闭对话并清理缓存
     */
    @PostMapping("/close-conversation")
    public void closeConversation(@RequestParam String conversationId) {
        conversationCacheService.closeConversation(conversationId);
    }

    /**
     * 创建聊天流 - 使用GET请求和缓存的消息历史
     *
     * @param content        用户输入的消息内容
     * @param model          使用的AI模型
     * @param conversationId 对话ID
     * @return 聊天响应流
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StreamResponseEvent> createChatStream(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "conversation_id", required = false) String conversationId) {

        // 创建ChatRequest对象
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setConversationId(conversationId);
        chatRequest.setModel(model);

        // 如果有新的用户消息，添加到缓存
        if (content != null && !content.trim().isEmpty() && conversationId != null) {
            ChatRequest.EnterMessage userMessage = new ChatRequest.EnterMessage();
            userMessage.setRole("user");
            userMessage.setContent(content.trim());
            conversationCacheService.addMessage(conversationId, userMessage);
        }

        // 从缓存获取历史消息
        List<ChatRequest.EnterMessage> cachedMessages = conversationId != null
                ? conversationCacheService.getMessages(conversationId)
                : new ArrayList<>();

        // 如果缓存为空且有内容，创建默认消息
        if (cachedMessages.isEmpty() && content != null && !content.trim().isEmpty()) {
            ChatRequest.EnterMessage defaultMessage = new ChatRequest.EnterMessage();
            defaultMessage.setRole("user");
            defaultMessage.setContent(content.trim());
            cachedMessages = new ArrayList<>();
            cachedMessages.add(defaultMessage);
        }

        // 设置消息历史
        chatRequest.setAdditionalMessages(cachedMessages);

        // 验证必要参数
        if (chatRequest.getAdditionalMessages() == null || chatRequest.getAdditionalMessages().isEmpty()) {
            List<ChatRequest.EnterMessage> messages = new ArrayList<>();
            ChatRequest.EnterMessage defaultMessage = new ChatRequest.EnterMessage();
            defaultMessage.setRole("user");
            defaultMessage.setContent("请生成一个问卷");
            messages.add(defaultMessage);
            chatRequest.setAdditionalMessages(messages);
        }

        return chatService.createChatStream(chatRequest, conversationId, model)
                .doOnNext(event -> {
                    // 如果是AI的响应内容，也可以选择缓存（可选）
                    if (event.getEventType().name().equals("done") && conversationId != null) {
                        // 这里可以添加AI响应的缓存逻辑，如果需要的话
                    }
                });
    }
}