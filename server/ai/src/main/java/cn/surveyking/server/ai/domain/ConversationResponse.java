package cn.surveyking.server.ai.domain;

import lombok.Data;
import java.util.Map;

/**
 * 会话响应
 *
 * @author zzr
 */
@Data
public class ConversationResponse {
    /**
     * 会话ID
     */
    private String id;
    
    /**
     * 创建时间
     */
    private Long createdAt;
    
    /**
     * 元数据
     */
    private Map<String, String> metaData;
}