package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.constant.ProjectPartnerTypeEnum;
import cn.surveyking.server.core.uitls.ClassUtils;
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

import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

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

	private List<String> projectSettingUpdateKeys;

	@Override
	public PaginationResponse<ProjectView> listProject(ProjectQuery query) {
		Page<Project> page = pageByQuery(query, Wrappers.<Project>lambdaQuery()
				.like(isNotBlank(query.getName()), Project::getName, query.getName())
				.eq(isNotBlank(query.getParentId()), Project::getParentId, query.getParentId())
				// 父id为空或者为 0 表示一级目录
				.and(isBlank(query.getParentId()),
						c -> c.isNull(Project::getParentId).or().eq(Project::getParentId, "0"))
				.eq(query.getMode() != null, Project::getMode, query.getMode())
				.exists(String.format(
						"SELECT 1 FROM t_project_partner t WHERE t.type in (1, 2) AND t.user_id = '%s' AND t.project_id = t_project.id",
						SecurityContextUtils.getUserId()))
				.orderByAsc(Project::getPriority, Project::getCreateAt));
		PaginationResponse<ProjectView> result = new PaginationResponse<>(page.getTotal(),
				projectViewMapper.toView(page.getRecords()));
		result.getList().forEach(view -> {
			if (ProjectModeEnum.folder.equals(view.getMode())) {
				view.setTotal(count(Wrappers.<Project>lambdaQuery().eq(Project::getParentId, view.getId())));
			}
			else {
				view.setTotal(answerMapper
						.selectCount(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, view.getId())));
			}
		});
		return result;
	}

	public ProjectView getProject(String id) {
		return projectViewMapper.toView(getById(id));
	}

	@Override
	public ProjectView addProject(ProjectRequest request) {
		Project project = projectViewMapper.fromRequest(request);
		String projectId = generateProjectId();
		project.setId(projectId);
		if (project.getSurvey() != null) {
			project.getSurvey().setId(projectId);
		}
		if (ProjectModeEnum.folder.equals(request.getMode())) {
			project.setPriority(
					count(Wrappers.<Project>lambdaQuery().eq(Project::getMode, ProjectModeEnum.folder)) + 1);
		}
		else {
			project.setPriority(
					count(Wrappers.<Project>lambdaQuery().ne(Project::getMode, ProjectModeEnum.folder)) + 1000);
		}
		save(project);

		ProjectPartnerRequest partnerRequest = new ProjectPartnerRequest();
		partnerRequest.setType(ProjectPartnerTypeEnum.OWNER.getType());
		partnerRequest.setProjectId(project.getId());
		partnerRequest.setUserIds(Collections.singletonList(SecurityContextUtils.getUserId()));
		projectPartnerService.addProjectPartner(partnerRequest);
		return projectViewMapper.toView(project);
	}

	private String generateProjectId() {
		String projectId = NanoIdUtils.randomNanoId(6);
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
		synchronized (request.getId().intern()) {
			Project project = projectViewMapper.fromRequest(request);
			if (request.getSettingKey() != null) {
				validateSettingKey(request.getSettingKey());
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
	}

	/**
	 * spel漏洞修复，只允许更新指定参数
	 * @param expressionString
	 */
	private void validateSettingKey(String expressionString) {
		if (projectSettingUpdateKeys == null) {
			projectSettingUpdateKeys = ClassUtils.flatClassFields(ProjectSetting.class, new ArrayList<>(), 2);
		}
		if (!projectSettingUpdateKeys.contains(expressionString)) {
			throw new ValidationException("非法的更新参数");
		}
	}

	@Override
	public void deleteProject(ProjectRequest request) {
		removeById(request.getId());
	}

	@Override
	public ProjectSetting getSetting(ProjectQuery query) {
		return null;
	}

	@Override
	public List<ProjectView> getDeleted(ProjectQuery query) {
		List<ProjectView> list = projectViewMapper
				.toView(getBaseMapper().selectLogicDeleted(SecurityContextUtils.getUserId()));
		list.forEach(view -> {
			if (!ProjectModeEnum.folder.equals(view.getMode())) {
				view.setTotal(answerMapper
						.selectCount(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, view.getId())));
			}
		});
		return list;
	}

	@Override
	public void batchDestroyProject(ProjectRequest request) {
		getBaseMapper().batchDestroy(request.getIds());
		// 删除项目参与者
		ProjectPartnerRequest deletePartnerRequest = new ProjectPartnerRequest();
		deletePartnerRequest.setProjectIds(request.getIds());
		projectPartnerService.deleteProjectPartner(deletePartnerRequest);
	}

	@Override
	public void restoreProject(ProjectRequest request) {
		getBaseMapper().restoreProject(request.getIds());
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
