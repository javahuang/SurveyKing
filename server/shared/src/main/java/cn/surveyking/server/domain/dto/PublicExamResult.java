package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/7/22
 */
@Data
public class PublicExamResult {

	private String name;

	private SurveySchema schema;

	/**
	 * 考试分数
	 */
	private Double examScore;

	/**
	 * 答题信息
	 */
	private AnswerMetaInfo metaInfo;

	/**
	 * 考试信息
	 */
	private AnswerExamInfo examInfo;

	/**
	 * 答案
	 */
	private LinkedHashMap answer;

	/**
	 * 排名信息
	 */
	private Integer rank;

}
