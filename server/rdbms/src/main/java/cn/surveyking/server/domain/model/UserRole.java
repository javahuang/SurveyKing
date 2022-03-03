package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
@TableName(value = "t_user_role", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class UserRole extends BaseModel {

	private String userId;

	private String roleId;

	private String userType;

	@JsonIgnore
	@TableField(exist = false)
	private boolean deleted = false;

}
