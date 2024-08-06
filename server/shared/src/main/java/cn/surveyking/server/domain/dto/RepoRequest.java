package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@Data
public class RepoRequest {

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

	/**
	 * 题库类型
	 */
	private String mode;

	private Boolean shared;

	/**
	 * 题库分类
	 */
	private String category;

	/**
	 * 是否是练习题库
	 */
	private Boolean isPractice;

}
