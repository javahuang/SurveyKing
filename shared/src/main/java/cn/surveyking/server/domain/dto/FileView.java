package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/9/8
 */
@Data
public class FileView {

	/**
	 * 附件id
	 */
	private String id;

	/**
	 * 文件名
	 */
	private String originalName;

	/**
	 * 预览地址
	 */
	private String previewUrl;

}
