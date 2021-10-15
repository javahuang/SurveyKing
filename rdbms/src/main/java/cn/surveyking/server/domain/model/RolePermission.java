package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
@TableName(value = "t_role_permission", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class RolePermission extends BaseModel {

	private String roleId;

	private String permissionCode;

}
