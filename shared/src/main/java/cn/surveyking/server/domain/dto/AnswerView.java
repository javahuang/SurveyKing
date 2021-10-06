package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class AnswerView {

	private String id;

	private String shortId;

	private LinkedHashMap answer;

	private List<Attachment> attachment;

	private AnswerMetaInfo metaInfo;

	/**
	 * 0 暂存 1 已完成
	 */
	private Integer tempSave;

	private Date createAt;

	private String createBy;

	private Date updateAt;

	private String updateBy;

	private Integer deleted;

	@Data
	public static class Attachment {

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

}
