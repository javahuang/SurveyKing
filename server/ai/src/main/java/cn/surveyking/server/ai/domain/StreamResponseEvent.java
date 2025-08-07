package cn.surveyking.server.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流式响应事件
 *
 * @author zzr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamResponseEvent {
    /**
     * 事件类型
     */
    private EventTypeEnum eventType;
    
    /**
     * 响应内容
     */
    private String content;
    
    /**
     * 推理内容（用于 DeepSeek 等模型）
     */
    private String reasoningContent;

    public StreamResponseEvent(EventTypeEnum eventType, String content) {
        this.eventType = eventType;
        this.content = content;
    }
}