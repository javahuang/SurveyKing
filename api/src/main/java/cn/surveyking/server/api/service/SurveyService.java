package cn.surveyking.server.api.service;

import cn.surveyking.server.api.domain.dto.ProjectQuery;
import cn.surveyking.server.api.domain.dto.PublicProjectView;

/**
 * @author javahuang
 * @date 2021/8/22
 */
public interface SurveyService {

	PublicProjectView loadProject(ProjectQuery query);

	PublicProjectView verifyPassword(ProjectQuery query);

}
