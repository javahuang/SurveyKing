package cn.surveyking.server.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 模型类型
 *
 * @author zzr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelType {
    /**
     * 模型显示名称
     */
    private String displayName;
    
    /**
     * 模型实际值
     */
    private String value;
    
    /**
     * 模型描述
     */
    private String description;
}