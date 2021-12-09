package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.ProjectView;

/**
 * @author javahuang
 * @date 2021/8/2
 */
public interface ProjectService {

	PaginationResponse<ProjectView> listProject(ProjectQuery filter);

	ProjectView getProject(ProjectQuery filter);

	ProjectView addProject(ProjectRequest project);

	void updateProject(ProjectRequest project);

	void deleteProject(String id);

	ProjectSetting getSetting(ProjectQuery filter);

}
