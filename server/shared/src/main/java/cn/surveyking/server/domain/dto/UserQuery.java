package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserQuery extends PageQuery {

	private String name;

	private String deptId;

	private String[] ids;

	private String roleId;

	private String neRoleId;

}
