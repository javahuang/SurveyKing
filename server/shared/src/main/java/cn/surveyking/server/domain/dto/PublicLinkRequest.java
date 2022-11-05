package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/11/05
 */
@Data
public class PublicLinkRequest {

	/**
	 * 问卷id
	 */
	private String projectId;

	/**
	 * 问题id
	 */
	private String questionId;

	/**
	 * 选项id
	 */
	private String optionId;

	/**
	 * 输入的文本
	 */
	private Object value;

}
