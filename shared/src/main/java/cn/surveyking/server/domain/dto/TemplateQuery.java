package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Data
public class TemplateQuery extends PageQuery {

	SurveySchemaType.QuestionType questionType = SurveySchemaType.QuestionType.Survey;

	private String name;

	/**
	 * 默认查询的是公共库
	 */
	private Integer shared = 1;

	List<String> categories = new ArrayList<>();

	List<String> tags = new ArrayList<>();

}
