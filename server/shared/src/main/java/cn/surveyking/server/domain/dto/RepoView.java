package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@Data
public class RepoView {

	private String id;

	/**
	 * 标题
	 */
	private String name;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 问卷/考试
	 */
	private String mode;

	/**
	 * 标签
	 */
	private String[] tag;

	/**
	 * 排序优先级
	 */
	private Integer priority;

	/**
	 * 设置
	 */
	private String setting;

	private Long total;

	/**
	 * 模板标签数量
	 */
	private List<TemplateTagTotalView> templateTags;

	/**
	 * 模板
	 */
	private List<RepoQuestionTypeTotalView> repoQuestionTypes;

	private Boolean shared;

	private String category;

	private Boolean isPractice;
}
