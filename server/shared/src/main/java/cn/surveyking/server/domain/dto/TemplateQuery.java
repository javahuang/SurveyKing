package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TemplateQuery extends PageQuery {

	/**
	 * 如果为空则查询普通的题型
	 */
	private SurveySchema.QuestionType questionType;

	private String name;

	/**
	 * 默认查询的是公共库
	 */
	private Integer shared = 1;

	private List<String> categories = new ArrayList<>();

	private List<String> tag = new ArrayList<>();

	private String repoId;

}
