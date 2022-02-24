package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.domain.model.Project;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/22
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

	private final ProjectMapper projectMapper;

	private final ProjectViewMapper projectViewMapper;

	private final AnswerService answerService;

	/**
	 * 如果需要验证密码，则只有密码输入正确之后才开始加载 schema
	 * @param projectId
	 * @return
	 */
	@Override
	public PublicProjectView loadProject(String projectId) {
		Project project = projectMapper.selectById(projectId);
		if (project == null) {
			throw new ErrorCodeException(ErrorCode.ProjectNotFound);
		}
		if (project != null && project.getSetting() != null && project.getSetting().getAnswerSetting() != null
				&& project.getSetting().getAnswerSetting().getPassword() != null) {
			project.setSurvey(null);
		}
		return projectViewMapper.toPublicProjectView(project);
	}

	@Override
	public PublicProjectView verifyPassword(ProjectQuery query) {
		Project project = projectMapper.selectById(query.getId());
		try {
			if (project.getSetting().getAnswerSetting().getPassword().equals(query.getPassword())) {
				return projectViewMapper.toPublicProjectView(project);
			}
			throw new InternalServerError("密码验证失败");
		}
		catch (Exception e) {
			throw new InternalServerError("密码验证失败");
		}
	}

	@Override
	public void validateProject(String projectId, ProjectSetting setting) {
		Long maxAnswers = setting.getAnswerSetting().getMaxAnswers();
		// 校验最大答案条数限制
		if (maxAnswers != null) {
			long totalAnswers = answerService.count(projectId);
			if (totalAnswers >= maxAnswers) {
				throw new ErrorCodeException(ErrorCode.ExceededMaxAnswers);
			}
			// 不将设置暴露给前端接口，不使用 @JsonProperty，否则设置页面回获取不到该值
			setting.getAnswerSetting().setMaxAnswers(null);
		}
		// 校验问卷是否已到结束时间
		Long endTime = setting.getAnswerSetting().getEndTime();
		if (endTime != null) {
			if (new Date().getTime() > endTime) {
				throw new ErrorCodeException(ErrorCode.ExceededEndTime);
			}
		}
	}

}
