package cn.surveyking.server.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目参与者
 *
 * @TableName t_project_partner
 */
@TableName(value = "t_project_partner")
@Data
public class ProjectPartner implements Serializable {

	/**
	 *
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 项目id
	 */
	@TableField(value = "project_id")
	private String projectId;

	/**
	 * 参与者类型
	 */
	@TableField(value = "type")
	private Integer type;

	/**
	 * 参与者id
	 */
	@TableField(value = "user_id")
	private String userId;

	/**
	 * 参与组id
	 */
	@TableField(value = "group_id")
	private String groupId;

	/**
	 * 数据权限
	 */
	@TableField(value = "data_permission")
	private String dataPermission;

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