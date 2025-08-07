package cn.surveyking.server.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 消息
 *
 * @author zzr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiMessage {
    /**
     * 会话ID
     */
    private String conversationId;
    
    /**
     * 消息角色
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
}