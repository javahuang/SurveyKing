package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/5/3
 */
@Data
public class RepoQuestionTypeTotalView {

	/**
	 * 问题类型
	 */
	private SurveySchema.QuestionType questionType;

	/**
	 * 数量
	 */
	private Long total;

}
