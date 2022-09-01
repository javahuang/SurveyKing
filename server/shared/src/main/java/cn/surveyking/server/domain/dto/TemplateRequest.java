package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequest {

	private String id;

	/**
	 * 模板标题
	 */
	private String name;

	/**
	 * 问题类型
	 */
	private SurveySchema.QuestionType questionType;

	/**
	 * 问题模板
	 */
	private SurveySchema template;

	/**
	 * 模板模式
	 */
	private ProjectModeEnum mode;

	/**
	 * 标签
	 */
	private String[] tag;

	/**
	 * 模板分类
	 */
	private String category;

	/**
	 * 预览地址
	 */
	private String previewUrl;

	/**
	 * 排序优先级，值越小优先级越高
	 */
	private Integer priority;

	/**
	 * 与其他用户共享
	 */
	private Integer shared;

	private String repoId;
	
	private List<String> ids;

}
