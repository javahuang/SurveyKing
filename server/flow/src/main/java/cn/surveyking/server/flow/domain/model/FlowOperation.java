package cn.surveyking.server.flow.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 流程操作
 *
 * @TableName t_flow_operation
 */
@TableName(value = "t_flow_operation", autoResultMap = true)
@Data
public class FlowOperation implements Serializable {

	/**
	 * 主键ID
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 流程实例Id
	 */
	@TableField(value = "instance_id")
	private String instanceId;

	/**
	 * 项目Id
	 */
	@TableField(value = "project_id")
	private String projectId;

	/**
	 * xml 节点Id
	 */
	@TableField(value = "activity_id")
	private String activityId;

	/**
	 * 新的任务节点 id
	 */
	@TableField(value = "new_activity_id")
	private String newActivityId;

	/**
	 * 任务Id
	 */
	@TableField(value = "task_id")
	private String taskId;

	/**
	 * 当前答案ID
	 */
	@TableField(value = "answer_id")
	private String answerId;

	/**
	 * 任务名称
	 */
	@TableField(value = "task_name")
	private String taskName;

	/**
	 * 任务类型
	 */
	@TableField(value = "task_type")
	private Integer taskType;

	/**
	 * 审批类型
	 */
	@TableField(value = "approval_type")
	private String approvalType;

	/**
	 * 注释内容
	 */
	@TableField(value = "comment")
	private String comment;

	/**
	 * 委托指定人
	 */
	@TableField(value = "delegate_assignee")
	private String delegateAssignee;

	/**
	 * 当前节点答案
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, value = "answer")
	private LinkedHashMap<String, Object> answer;

	/**
	 * 是否是最新的一条操作记录
	 */
	private Boolean latest;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_at")
	private Date createAt;

	/**
	 *
	 */
	@TableField(value = "create_by")
	private String createBy;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_at")
	private Date updateAt;

	/**
	 *
	 */
	@TableField(value = "update_by")
	private String updateBy;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

}