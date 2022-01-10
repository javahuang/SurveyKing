package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class ProjectRequest {

	private String id;

	private String name;

	private SurveySchema survey;

	private ProjectSetting setting;

	// 1已发布 0未发布
	private Integer status;

	private String belongGroup;

}
