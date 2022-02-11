package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/2/11
 */
@Data
public class SystemInfoRequest {

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

}
