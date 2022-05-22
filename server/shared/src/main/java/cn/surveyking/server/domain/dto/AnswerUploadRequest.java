package cn.surveyking.server.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author javahuang
 * @date 2022/5/21
 */
@Data
public class AnswerUploadRequest {

	private MultipartFile file;

	private String projectId;

	/**
	 * 是否根据行头自动创建 schema
	 */
	private Boolean autoSchema;

}
