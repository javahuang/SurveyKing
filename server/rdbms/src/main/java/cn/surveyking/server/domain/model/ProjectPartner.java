package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目参与者
 *
 * @TableName t_project_partner
 */
@TableName(value = "t_project_partner")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectPartner extends BaseModel {

	/**
	 * 项目id
	 */
	@TableField(value = "project_id")
	private String projectId;

	/**
	 * 参与者类型 {@link cn.surveyking.server.core.constant.ProjectPartnerTypeEnum}
	 */
	@TableField(value = "type")
	private Integer type;

	/**
	 * 回答状态 0未访问 1已访问 2已答题
	 */
	@TableField(value = "status")
	private Integer status;

	/**
	 * 参与者id，内部用户白名单使用
	 */
	@TableField(value = "user_id")
	private String userId;

	/**
	 * 参与者姓名，导入白名单使用
	 */
	@TableField(value = "user_name")
	private String userName;

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

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private Boolean deleted = false;

}