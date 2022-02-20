package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/1/25
 */
@Data
public class DatabaseInitProperties {

	/** 数据库用户名 */
	private String username;

	/** 数据库密码 */
	private String password;

	/** 数据库名称 */
	private String dbName;

	/** 驱动名称 */
	private String driverClassName;

	/** 数据库ip */
	private String dbIp = "127.0.0.1";

	/** 数据库端口号 */
	private Integer dbPort;

	/** 服务端口号 */
	private Integer serverPort = 1991;

	private String applicationName = "SurveyKing";

	/** 数据库 url */
	private String dataSourceUrl;

}
