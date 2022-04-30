package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.TagCategoryEnum;
import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/18
 */
@Data
public class TagQuery {

	private String name;

	private TagCategoryEnum category = TagCategoryEnum.repo;

	/**
	 * 默认查询的是公共库
	 */
	private Integer shared = 1;

	SurveySchema.QuestionType questionType = SurveySchema.QuestionType.Survey;

}
