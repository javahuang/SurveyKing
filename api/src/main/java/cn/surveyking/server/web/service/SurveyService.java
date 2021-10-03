package cn.surveyking.server.web.service;

import cn.surveyking.server.web.domain.dto.ProjectQuery;
import cn.surveyking.server.web.domain.dto.PublicProjectView;

/**
 * @author javahuang
 * @date 2021/8/22
 */
public interface SurveyService {

	PublicProjectView loadProject(ProjectQuery query);

	PublicProjectView verifyPassword(ProjectQuery query);

}
