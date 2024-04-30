package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/11
 */
@Data
public class SystemInfo {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 系统名称
	 */
	private String name;

	/**
	 * 系统描述信息
	 */
	private String description;

	/**
	 * 图标
	 */
	private String avatar;

	/**
	 * 默认语言
	 */
	private String locale;

	/**
	 * 默认语言
	 */
	private String version;

	private RegisterInfo registerInfo;

	private SystemSetting setting;

	private String publicKey;

	@Data
	public static class RegisterInfo {

		/**
		 * 是否开启注册
		 */
		private Boolean registerEnabled;

		/**
		 * 注册用户可选角色列表
		 */
		private List<String> roles = new ArrayList<>();

		/**
		 * 开启强密码验证
		 */
		private Boolean strongPasswordEnabled;

	}

	@Data
	public static class SystemSetting {

		/**
		 * 是否开启验证码
		 */
		private Boolean captchaEnabled;

		/**
		 * 版权
		 */
		private String copyright;

		/**
		 * 备案号
		 */
		private String recordNum;

	}

}
