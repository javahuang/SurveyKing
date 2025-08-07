package cn.surveyking.server.ai.service;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import cn.surveyking.server.ai.domain.ChatRequest;
import cn.surveyking.server.ai.domain.ConversationRequest;
import cn.surveyking.server.ai.domain.ConversationResponse;
import cn.surveyking.server.ai.domain.StreamResponseEvent;
import cn.surveyking.server.ai.domain.AiMessage;
import cn.surveyking.server.ai.domain.ModelType;
import reactor.core.publisher.Flux;

/**
 * AI 聊天服务
 *
 * @author zzr
 */
public interface AiChatService {

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean isEnabled();

    /**
     * 创建会话
     *
     * @param conversationRequest 会话请求
     * @return 会话响应
     */
    ConversationResponse createConversation(ConversationRequest conversationRequest);

    /**
     * 创建聊天流
     *
     * @param chatRequest    聊天请求
     * @param conversationId 会话ID
     * @return 聊天响应流
     */
    Flux<StreamResponseEvent> createChatStream(ChatRequest chatRequest, String conversationId, String model,
            Consumer<AiMessage> consumer);

    /**
     * 获取 Prompt
     *
     * @param modelType 模型类型
     * @param modelId   模型ID
     * @return Prompt 消息，如果当前实现不需要 Prompt，则返回 null
     */
    default AiMessage getPrompt(String modelType, String modelId) {
        return null;
    }

    /**
     * 获取当前服务支持的模型列表
     *
     * @return 支持的模型ID列表
     */
    default List<ModelType> getSupportedModels() {
        // 默认情况下，返回一个空列表，具体实现类需要覆盖此方法
        return Collections.emptyList();
    }
}