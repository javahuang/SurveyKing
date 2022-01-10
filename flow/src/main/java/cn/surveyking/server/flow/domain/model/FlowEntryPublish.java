package cn.surveyking.server.flow.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_flow_entry_publish
 */
@TableName(value = "t_flow_entry_publish", autoResultMap = true)
@Data
public class FlowEntryPublish implements Serializable {

	/**
	 * 主键
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 流程Id
	 */
	@TableField(value = "entry_id")
	private String entryId;

	/**
	 * 流程引擎的定义Id
	 */
	@TableField(value = "process_definition_id")
	private String processDefinitionId;

	/**
	 * 发布版本
	 */
	@TableField(value = "publish_version")
	private Integer publishVersion;

	/**
	 * 激活状态
	 */
	@TableField(value = "active_status")
	private Boolean activeStatus;

	/**
	 * 是否为主版本
	 */
	@TableField(value = "main_version")
	private Boolean mainVersion;

	/**
	 * 发布时间
	 */
	@TableField(value = "publish_time")
	private Date publishTime;

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