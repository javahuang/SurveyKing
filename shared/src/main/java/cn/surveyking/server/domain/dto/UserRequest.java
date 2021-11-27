package cn.surveyking.server.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/15
 */
@Data
public class UserRequest {

	private String id;

	private String orgId;

	@NotNull
	private String name;

	/** 登录账号 */
	@NotNull
	private String username;

	/** 密码 */
	private String password;

	private List<String> roles = new ArrayList<>();

	private String phone;

	private String email;

	private String gender;

	private Integer status;

	private List<UserPositionRequest> userPositions = new ArrayList<>();

}
