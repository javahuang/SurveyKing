package cn.surveyking.server.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author javahuang
 * @date 2022/5/5
 */
@Data
public class UploadFileRequest {

	/**
	 * 同一个问卷内的附件存放到同一个文件夹，basePath 为问卷 id
	 */
	private String basePath;

	private MultipartFile file;

	private int fileType;
	
	private String id;

}
