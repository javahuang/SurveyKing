package cn.surveyking.server.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
public class AuthRequest {

	@NotNull
	private String username;

	@NotNull
	private String password;

}
