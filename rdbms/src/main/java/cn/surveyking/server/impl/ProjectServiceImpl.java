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
import cn.surveyking.server.mapper.ProjectPartnerMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.ProjectPartnerService;
import cn.surveyking.server.service.ProjectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private final ProjectPartnerMapper projectPartnerMapper;

	private final ProjectPartnerService projectPartnerService;

	@Override
	public PaginationResponse<ProjectView> listProject(ProjectQuery query) {
		Page<Project> page = pageByQuery(query, Wrappers.<Project>lambdaQuery()
				.eq(isNotBlank(query.getName()), Project::getName, query.getName())
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

	public ProjectView getProject(ProjectQuery query) {
		return projectViewMapper.toProjectView(getById(query.getId()));
		// List<Answer> answers = answerMapper
		// .selectList(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId,
		// query.getId())
		// .select(Answer::getMetaInfo,
		// Answer::getCreateAt).orderByDesc(Answer::getCreateAt));
		// result.setTotal((long) answers.size());
		// long totalDuration = 0;
		// int totalOfToday = 0;
		// for (int i = 0; i < answers.size(); i++) {
		// Answer current = answers.get(i);
		// if (i == 0) {
		// result.setLastUpdate(current.getCreateAt().getTime());
		// }
		// if (current.getCreateAt().getTime() >
		// LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
		// .toEpochMilli()) {
		// totalOfToday++;
		// }
		// totalDuration += current.getMetaInfo().getAnswerInfo().getEndTime()
		// - current.getMetaInfo().getAnswerInfo().getStartTime();
		// }
		// if (totalDuration > 0) {
		// result.setAverageDuration(totalDuration / answers.size());
		// result.setTotalOfToday(totalOfToday);
		// }
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
	public void updateProject(ProjectRequest request) {
		updateById(projectViewMapper.fromRequest(request));
	}

	@Override
	public void deleteProject(String id) {
		removeById(id);
	}

	@Override
	public ProjectSetting getSetting(ProjectQuery query) {
		return null;
	}

}
