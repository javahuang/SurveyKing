package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/12/18
 */
@Data
public class Attachment {

	/**
	 * 附件id
	 */
	private String id;

	/**
	 * 附件原始名字
	 */
	private String originalName;

	/**
	 * 内容类型
	 */
	private String contentType;

}
