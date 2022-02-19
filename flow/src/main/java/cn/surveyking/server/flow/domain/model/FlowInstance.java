package cn.surveyking.server.flow.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例
 *
 * @TableName t_flow_instance
 */
@TableName(value = "t_flow_instance", autoResultMap = true)
@Data
public class FlowInstance implements Serializable {

	/**
	 * 流程实例ID
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 项目id
	 */
	@TableField(value = "project_id")
	private String projectId;

	/**
	 * 答案id
	 */
	@TableField(value = "answer_id")
	private String answerId;

	/**
	 * 当前状态
	 */
	@TableField(value = "status")
	private Integer status;

	/**
	 * 当前所处的审批阶段，审批节点名称
	 */
	@TableField(value = "approval_stage")
	private String approvalStage;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_at")
	private Date createAt;

	/**
	 * 创建人
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