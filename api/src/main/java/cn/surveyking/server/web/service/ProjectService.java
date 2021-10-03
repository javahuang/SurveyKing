package cn.surveyking.server.web.service;

import cn.surveyking.server.web.domain.dto.ProjectQuery;
import cn.surveyking.server.web.domain.dto.ProjectView;
import cn.surveyking.server.web.domain.model.Project;
import cn.surveyking.server.web.domain.model.ProjectSetting;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/2
 */
public interface ProjectService {

	List<ProjectView> listProject(ProjectQuery filter);

	ProjectView getProject(ProjectQuery filter);

	String addProject(Project project);

	void updateProject(Project project);

	void deleteProject(String id);

	ProjectSetting getSetting(ProjectQuery filter);

}
