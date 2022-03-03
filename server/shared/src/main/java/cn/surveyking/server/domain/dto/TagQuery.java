package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/18
 */
@Data
public class TagQuery {

	private String name;

	/**
	 * 默认查询的是公共库
	 */
	private Integer shared = 1;

	SurveySchema.QuestionType questionType = SurveySchema.QuestionType.Survey;

}
