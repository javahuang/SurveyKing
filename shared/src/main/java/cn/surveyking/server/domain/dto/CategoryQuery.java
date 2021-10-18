package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/18
 */
@Data
public class CategoryQuery {

	private String name;

	private Integer shared = 1;

	SurveySchemaType.QuestionType questionType = SurveySchemaType.QuestionType.Survey;

}
