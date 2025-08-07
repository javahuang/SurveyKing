package cn.surveyking.server.ai.domain;

import lombok.Data;
import java.util.List;

/**
 * 聊天请求
 *
 * @author zzr
 */
@Data
public class ChatRequest {
    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 附加消息列表（包含历史会话记录）
     */
    private List<EnterMessage> additionalMessages;

    @Data
    public static class EnterMessage {
        /**
         * 消息角色
         */
        private String role;

        /**
         * 消息内容
         */
        private String content;
    }
}