package cn.surveyking.server.ai.service;

import cn.surveyking.server.ai.domain.ChatRequest;
import cn.surveyking.server.ai.domain.ConversationRequest;
import cn.surveyking.server.ai.domain.ConversationResponse;
import cn.surveyking.server.ai.domain.ModelType;
import cn.surveyking.server.ai.domain.StreamResponseEvent;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 聊天服务
 *
 * @author zzr
 */
public interface ChatService {

    /**
     * 获取所有模型类型
     *
     * @return 模型类型列表
     */
    List<ModelType> getAllModelTypes();

    /**
     * 创建会话
     *
     * @param conversationRequest 会话请求
     * @param model               模型
     * @return 会话响应
     */
    ConversationResponse createConversation(ConversationRequest conversationRequest, String model);

    /**
     * 创建聊天流
     *
     * @param chatRequest    聊天请求
     * @param conversationId 会话ID
     * @param model          模型
     * @return 聊天响应流
     */
    Flux<StreamResponseEvent> createChatStream(ChatRequest chatRequest, String conversationId, String model);
}