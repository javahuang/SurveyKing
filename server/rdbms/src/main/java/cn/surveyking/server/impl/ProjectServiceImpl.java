package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.uitls.NanoIdUtils;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.domain.model.Answer;
import cn.surveyking.server.domain.model.Project;
import cn.surveyking.server.mapper.AnswerMapper;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.ProjectPartnerService;
import cn.surveyking.server.service.ProjectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collections;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/8/3
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl extends BaseService<ProjectMapper, Project> implements ProjectService {

	private final AnswerMapper answerMapper;

	private final ProjectViewMapper projectViewMapper;

	private final ProjectPartnerService projectPartnerService;

	private SpelExpressionParser spelParser = new SpelExpressionParser();

	@Override
	public PaginationResponse<ProjectView> listProject(ProjectQuery query) {
		Page<Project> page = pageByQuery(query, Wrappers.<Project>lambdaQuery()
				.like(isNotBlank(query.getName()), Project::getName, query.getName())
				.exists(String.format(
						"select 1 from t_project_partner t where t.user_id = '%s' and t.project_id = t_project.id",
						SecurityContextUtils.getUserId()))
				.orderByAsc(Project::getCreateAt));
		PaginationResponse<ProjectView> result = new PaginationResponse<>(page.getTotal(),
				projectViewMapper.toProjectView(page.getRecords()));
		result.getList().forEach(view -> {
			view.setTotal(
					answerMapper.selectCount(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, view.getId())));
		});
		return result;
	}

	public ProjectView getProject(String id) {
		return projectViewMapper.toProjectView(getById(id));
	}

	@Override
	public ProjectView addProject(ProjectRequest request) {
		Project project = projectViewMapper.fromRequest(request);
		project.setId(generateProjectId());
		save(project);

		ProjectPartnerRequest partnerRequest = new ProjectPartnerRequest();
		partnerRequest.setType(AppConsts.ProjectPartnerType.OWNER);
		partnerRequest.setProjectId(project.getId());
		partnerRequest.setUserIds(Collections.singletonList(SecurityContextUtils.getUserId()));
		projectPartnerService.addProjectPartner(partnerRequest);
		return projectViewMapper.toProjectView(project);
	}

	private String generateProjectId() {
		String projectId = NanoIdUtils.randomNanoId();
		// 不要以数字开头，否则工作流 xml 保存会报错
		if (Character.isDigit(projectId.charAt(0))) {
			return generateProjectId();
		}
		if (getById(projectId) != null) {
			return generateProjectId();
		}
		return projectId;
	}

	@Override
	@SneakyThrows
	public void updateProject(ProjectRequest request) {
		Project project = projectViewMapper.fromRequest(request);
		if (request.getSettingKey() != null) {
			// 实现单个设置的更新
			ProjectSetting setting = getById(request.getId()).getSetting();
			spelParser.parseExpression(request.getSettingKey()).setValue(setting, request.getSettingValue());
			project.setSetting(setting);
			// 同步更新项目状态
			if ("status".equals(request.getSettingKey())) {
				project.setStatus((Integer) request.getSettingValue());
			}
		}
		updateById(project);
	}

	@Override
	public void deleteProject(String id) {
		removeById(id);
	}

	@Override
	public ProjectSetting getSetting(ProjectQuery query) {
		return null;
	}

	private <T> T merge(T local, T remote) throws IllegalAccessException, InstantiationException {
		Class<?> clazz = local.getClass();
		Object merged = clazz.newInstance();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			Object localValue = field.get(local);
			Object remoteValue = field.get(remote);
			switch (field.getType().getSimpleName()) {
			case "Integer":
			case "String":
			case "Boolean":
			case "Long":
			case "LinkedHashMap":
				field.set(merged, (remoteValue != null) ? remoteValue : localValue);
				break;
			default:
				field.set(merged, merge(localValue, remoteValue));
			}
		}
		return (T) merged;
	}

}
