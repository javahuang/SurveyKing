package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 答卷页面
 *
 * @author javahuang
 * @date 2021/8/26
 */
@Data
public class PublicProjectView {

	private String id;

	private SurveySchema survey;

	private Integer status;

	private ProjectSetting setting;

	private String name;

	private ProjectModeEnum mode;

	private Boolean passwordRequired;

	private Boolean loginRequired;

	private Date createAt;

	private String submittedHtml;

	private String answerId;

	LinkedHashMap<String, Object> answer;

}
