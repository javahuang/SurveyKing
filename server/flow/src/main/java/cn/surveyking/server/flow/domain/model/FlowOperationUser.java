package cn.surveyking.server.flow.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_flow_operation_user
 */
@TableName(value = "t_flow_operation_user")
@Data
public class FlowOperationUser implements Serializable {

	/**
	 * 节点id
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 操作id
	 */
	@TableField(value = "operation_id")
	private String operationId;

	/**
	 * 用户id
	 */
	@TableField(value = "user_id")
	private String userId;

	/**
	 * 组id
	 */
	@TableField(value = "group_id")
	private String groupId;

	/**
	 * 用户类型
	 */
	@TableField(value = "link_type")
	private String linkType;

	/**
	 * 是否最新记录
	 */
	@TableField(value = "latest")
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