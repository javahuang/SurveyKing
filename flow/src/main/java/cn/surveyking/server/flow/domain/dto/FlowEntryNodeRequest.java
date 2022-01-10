package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/1/6
 */
@Data
public class FlowEntryNodeRequest {

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 项目id
	 */
	private String projectId;

	/**
	 * 任务对应的xml节点id
	 */
	private String activityId;

	/**
	 * 流程节点类型
	 */
	private Integer taskType;

	/**
	 * 字段权限
	 */
	private LinkedHashMap<String, Integer> fieldPermission;

	/**
	 * 流程设置
	 */
	private FlowNodeSetting setting;

	/**
	 * 授权用户
	 */
	private String[] identity;

	/**
	 * 表达式
	 */
	private String expression;

}
