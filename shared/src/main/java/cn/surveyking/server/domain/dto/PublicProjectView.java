package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * 答卷页面
 *
 * @author javahuang
 * @date 2021/8/26
 */
@Data
public class PublicProjectView {

	private SurveySchema survey;

	private Integer status;

	private ProjectSetting.AnswerSetting setting;

	private String name;

	private Boolean passwordRequired;

	private Date createAt;

}
