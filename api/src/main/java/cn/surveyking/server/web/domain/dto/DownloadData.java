package cn.surveyking.server.web.domain.dto;

import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

/**
 * @author javahuang
 * @date 2021/8/10
 */
@Data
public class DownloadData {

	private String fileName;

	private Resource resource;

	private MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

}
