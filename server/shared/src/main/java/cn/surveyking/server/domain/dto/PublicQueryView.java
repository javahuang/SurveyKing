package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/5/15
 */
@Data
public class PublicQueryView {

	/**
	 * 展示的查询表单
	 */
	private SurveySchema schema;

	/**
	 * 答案
	 */
	private List<PublicAnswerView> answers;

	private LinkedHashMap<String, Integer> fieldPermission;

}
