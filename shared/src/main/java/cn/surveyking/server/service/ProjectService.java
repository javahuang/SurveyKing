package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.ProjectView;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/2
 */
public interface ProjectService {

	List<ProjectView> listProject(ProjectQuery filter);

	ProjectView getProject(ProjectQuery filter);

	String addProject(ProjectRequest project);

	void updateProject(ProjectRequest project);

	void deleteProject(String id);

	ProjectSetting getSetting(ProjectQuery filter);

}
