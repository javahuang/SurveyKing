package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.PublicProjectView;

/**
 * @author javahuang
 * @date 2021/8/22
 */
public interface SurveyService {

	PublicProjectView loadProject(String projectId);

	PublicProjectView verifyPassword(ProjectQuery query);

	void validateProject(String projectId, ProjectSetting setting);

}
