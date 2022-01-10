package cn.surveyking.server.flow.domain.model;

import cn.surveyking.server.flow.domain.dto.FlowNodeSetting;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 流程节点
 *
 * @TableName t_flow_entry_node
 */
@TableName(value = "t_flow_entry_node", autoResultMap = true)
@Data
public class FlowEntryNode implements Serializable {

	/**
	 * 节点id
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 节点名称
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 项目id
	 */
	@TableField(value = "project_id")
	private String projectId;

	/**
	 * 任务对应的xml节点id
	 */
	@TableField(value = "activity_id")
	private String activityId;

	/**
	 * 流程节点类型
	 */
	@TableField(value = "task_type")
	private Integer taskType;

	/**
	 * 字段权限
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, value = "field_permission")
	private LinkedHashMap<String, Integer> fieldPermission;

	/**
	 * 流程设置
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, value = "setting")
	private FlowNodeSetting setting;

	/**
	 * 授权用户
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, value = "identity")
	private String[] identity;

	/**
	 * 表达式
	 */
	@TableField(value = "expression")
	private String expression;

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