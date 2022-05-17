package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/5/15
 */
@Data
public class PublicQueryRequest {

	private String id;

	private String resultId;

	/**
	 *
	 */
	private LinkedHashMap answer;

}
