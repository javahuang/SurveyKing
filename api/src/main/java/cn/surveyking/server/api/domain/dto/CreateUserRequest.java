package cn.surveyking.server.api.domain.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
public class CreateUserRequest {

	@NotBlank
	@Email
	private String username;

	@NotBlank
	private String fullName;

	@NotBlank
	private String password;

	@NotBlank
	private String rePassword;

	private String phone;

	private String gender;

	private Set<String> authorities;

}
