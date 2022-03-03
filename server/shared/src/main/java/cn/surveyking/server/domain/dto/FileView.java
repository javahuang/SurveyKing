package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonIgnore
	private String filePath;

}
