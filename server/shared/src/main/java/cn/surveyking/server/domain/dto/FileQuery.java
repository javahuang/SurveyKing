package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.AppConsts;
import lombok.Data;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/2
 */
@Data
public class FileQuery {

	public FileQuery() {
	}

	public FileQuery(String id) {
		this.id = id;
	}

	Integer type;

	List<String> ids;

	/**
	 * 文件id
	 */
	String id;

	AppConsts.DispositionTypeEnum dispositionType;

	HttpHeaders headers;

}
