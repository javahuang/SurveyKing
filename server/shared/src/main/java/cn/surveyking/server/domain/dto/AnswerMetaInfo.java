package cn.surveyking.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

		private String cookie;

	}

}
