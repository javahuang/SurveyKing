package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/7/24
 */
@Data
public class MyTaskView {

	private String id;

	/**
	 * 项目ID
	 */
	private String projectId;

	/**
	 * 答案ID
	 */
	private String answerId;

	/**
	 * 项目名称
	 */
	private String name;

	/**
	 * 考试开始时间
	 */
	private Long examStartTime;

	/**
	 * 考试结束时间
	 */
	private Long examEndTime;

	/**
	 * 开始时间
	 */
	private Long startTime;

	/**
	 * 结束时间
	 */
	private Long endTime;

	/**
	 * 回答状态 0未访问 1已访问 2已答题
	 */
	private Integer status;

	/**
	 * 考试分数
	 */
	private Double examScore;

	/**
	 * 卷面分
	 */
	private Double examTotal;

}
