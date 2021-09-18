package cn.surveyking.server.api.domain.model;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
public class AnswerMetaInfo {

	private ClientInfo clientInfo;

	private AnswerInfo answerInfo;

	@Data
	public static class AnswerInfo {

		private long startTime;

		private long endTime;

	}

	@Data
	public static class ClientInfo {

		private String agent;

		private String browser;

		private String platformVersion;

		private String browserVersion;

		private String platform;

		private String remoteIp;

		/** 地区 */
		private String region;

		private String deviceType;

	}

}
