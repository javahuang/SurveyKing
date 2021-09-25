package cn.surveyking.server.api.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Data
public class TemplateView {

	private String id;

	private String name;

	private String questionType;

	private SurveySchemaType template;

	private String category;

	/**
	 * 只有所有者才能对模板进行删除操作
	 */
	private boolean owner;

}
