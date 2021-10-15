package cn.surveyking.server.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
public class CreateUserRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String name;

	@NotBlank
	private String password;

	@NotBlank
	private String rePassword;

	private String phone;

	private String gender;

	private Set<String> authorities;

}
