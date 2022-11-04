package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.AppConsts;
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

	private String captchaVerification;

	/**
	 * 默认认证方式，密码认证
	 */
	private AppConsts.AUTH_TYPE authType = AppConsts.AUTH_TYPE.PWD;

}
