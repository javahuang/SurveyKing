package cn.surveyking.server.ai.domain;

import lombok.Data;

/**
 * 会话请求
 *
 * @author zzr
 */
@Data
public class ConversationRequest {
    /**
     * 会话标题
     */
    private String title;
    
    /**
     * 模型类型
     */
    private String modelType;
}