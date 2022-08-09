package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class ProjectRequest {

	private String id;

	private String name;

	private ProjectModeEnum mode;

	private SurveySchema survey;

	private ProjectSetting setting;

	// 1已发布 0未发布
	private Integer status;

	private String belongGroup;

	private String settingKey;

	private Object settingValue;

	private String parentId;

	private List<String> ids;

}
