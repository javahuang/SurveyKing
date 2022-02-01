package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/15
 */
@Data
public class UserRequest {

	private String id;

	private String deptId;

	private String name;

	private String avatar;

	private String profile;

	/** 登录账号 */
	private String username;

	/** 密码 */
	private String password;

	/** 密码修改原密码 */
	private String oldPassword;

	private List<String> roles;

	private String phone;

	private String email;

	private String gender;

	private Integer status;

	private List<UserPositionRequest> userPositions;

}
