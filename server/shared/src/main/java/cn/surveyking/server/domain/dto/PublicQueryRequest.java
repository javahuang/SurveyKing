package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 公开查询支持两种答案查询方式，1、通过查询表单提交参数 2、通过 url 参数
 *
 * @author javahuang
 * @date 2022/5/15
 */
@Data
public class PublicQueryRequest {

	private String id;

	private String resultId;

	/**
	 * 查询表单提交答案参数
	 */
	private LinkedHashMap answer;

	/**
	 * url 查询参数
	 */
	private Map<String, String> query = new LinkedHashMap<>();

}
