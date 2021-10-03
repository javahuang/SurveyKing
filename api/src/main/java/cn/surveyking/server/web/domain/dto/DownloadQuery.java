package cn.surveyking.server.web.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author javahuang
 * @date 2021/9/17
 */
@Data
public class DownloadQuery {

	/**
	 * 问卷id
	 */
	@NotNull
	private String shortId;

	/**
	 * 当前答案id
	 */
	private String answerId;

	@NotNull
	private DownloadType type;

	public enum DownloadType {

		/**
		 * 问卷答案、问卷附件
		 */
		SURVEY_ANSWER, ANSWER_ATTACHMENT

	}

}
