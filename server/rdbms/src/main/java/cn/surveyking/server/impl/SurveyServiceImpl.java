package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.Tuple2;
import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.uitls.*;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.ProjectService;
import cn.surveyking.server.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author javahuang
 * @date 2021/8/22
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

	private final ProjectService projectService;

	private final ProjectViewMapper projectViewMapper;

	private final AnswerService answerService;

	/**
	 * answerService 如果需要验证密码，则只有密码输入正确之后才开始加载 schema
	 * @param projectId
	 * @return
	 */
	@Override
	public PublicProjectView loadProject(String projectId) {
		ProjectView project = projectService.getProject(projectId);
		if (project == null) {
			throw new ErrorCodeException(ErrorCode.ProjectNotFound);
		}
		PublicProjectView projectView = projectViewMapper.toPublicProjectView(project);

		// 需要密码答卷
		if (project != null && project.getSetting() != null && project.getSetting().getAnswerSetting() != null
				&& project.getSetting().getAnswerSetting().getPassword() != null) {
			projectView.setSurvey(null);
			projectView.setPasswordRequired(true);
		}

		// 需要登录答卷
		if (Boolean.TRUE.equals(projectView.getSetting().getAnswerSetting().getLoginRequired())
				&& !SecurityContextUtils.isAuthenticated()) {
			projectView.setLoginRequired(true);
			projectView.setSurvey(null);
		}

		// 允许修改答案
		if (Boolean.TRUE.equals(projectView.getSetting().getSubmittedSetting().getEnableUpdate())
				&& SecurityContextUtils.isAuthenticated()) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(projectId);
			answerQuery.setLatest(true);
			AnswerView latestAnswer = answerService.getAnswer(answerQuery);
			if (latestAnswer != null) {
				projectView.setAnswerId(latestAnswer.getId());
			}
		}
		// 校验问卷
		validateProject(project);
		return projectView;
	}

	@Override
	public PublicProjectView verifyPassword(ProjectQuery query) {
		ProjectView project = projectService.getProject(query.getId());
		if (project.getSetting().getAnswerSetting().getPassword().equals(query.getPassword())) {
			return projectViewMapper.toPublicProjectView(project);
		}
		throw new ErrorCodeException(ErrorCode.ValidationError);
	}

	/**
	 * 根据问卷设置校验问卷
	 * @param project
	 * @return
	 */
	private ProjectSetting validateProject(ProjectView project) {
		ProjectSetting setting = project.getSetting();
		String projectId = project.getId();
		if (setting.getStatus() == 0) {
			throw new ErrorCodeException(ErrorCode.SurveySuspend);
		}

		Long maxAnswers = setting.getAnswerSetting().getMaxAnswers();
		// 校验最大答案条数限制
		if (maxAnswers != null) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(project.getId());
			long totalAnswers = answerService.count(answerQuery);
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
		// 如果需要登录，则使用账号进行限制
		if (setting.getAnswerSetting().getLoginLimit() != null
				&& Boolean.TRUE.equals(setting.getAnswerSetting().getLoginRequired())) {
			validateLoginLimit(projectId, setting);
		}
		// cookie 限制
		if (setting.getAnswerSetting().getCookieLimit() != null) {
			validateCookieLimit(projectId, setting);
		}
		// ip 限制
		if (setting.getAnswerSetting().getIpLimit() != null) {
			validateIpLimit(projectId, setting);
		}
		validateExamSetting(project);
		return setting;
	}

	private void validateExamSetting(ProjectView project) {
		ProjectSetting.ExamSetting examSetting = project.getSetting().getExamSetting();
		if (examSetting == null || !ProjectModeEnum.exam.equals(project.getMode())) {
			return;
		}
		// 校验考试开始时间
		if (examSetting.getStartTime() != null && new Date(examSetting.getStartTime()).compareTo(new Date()) > 0) {
			throw new ErrorCodeException(ErrorCode.ExamUnStarted);
		}
		// 校验考试结束时间
		if (examSetting.getEndTime() != null && new Date(examSetting.getEndTime()).compareTo(new Date()) < 0) {
			throw new ErrorCodeException(ErrorCode.ExamFinished);
		}

	}

	@Override
	public PublicStatisticsView statProject(ProjectQuery query) {
		AnswerQuery answerQuery = new AnswerQuery();
		answerQuery.setProjectId(query.getId());
		answerQuery.setPageSize(-1);
		List<AnswerView> answers = answerService.listAnswer(answerQuery).getList();
		ProjectView project = projectService.getProject(query.getId());
		return new ProjectStatHelper(project.getSurvey(), answers).stat();
	}

	@Override
	public PublicAnswerView saveAnswer(AnswerRequest answer, HttpServletRequest request) {
		String projectId = answer.getProjectId();

		PublicAnswerView result = new PublicAnswerView();
		ProjectView project = projectService.getProject(projectId);
		ProjectSetting setting = project.getSetting();

		String answerId = validateAndGetLatestAnswer(project);
		answer.setId(answerId);
		AnswerView answerView = answerService.saveAnswer(answer, request);

		// 登录用户无需显示修改答案的二维码
		if (Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())
				&& !SecurityContextUtils.isAuthenticated()) {
			result.setAnswerId(answerView.getId());
		}
		if (ProjectModeEnum.exam.equals(project.getMode())) {
			result.setExamScore(answerView.getExamScore());
			result.setQuestionScore(answerView.getExamInfo().getQuestionScore());
		}

		return result;
	}

	@Override
	public PublicAnswerView loadAnswer(AnswerQuery query) {
		ProjectSetting setting = null;
		try {
			ProjectView project = projectService.getProject(query.getProjectId());
			setting = validateProject(project);
		}
		catch (ErrorCodeException e) {
			// 401 开头的是校验问卷限制，修改答案的时候无需校验
			if (!(e.getErrorCode().code + "").startsWith("401")) {
				throw e;
			}
		}
		if (!Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
			throw new ErrorCodeException(ErrorCode.AnswerChangeDisabled);
		}
		PublicAnswerView answerView = new PublicAnswerView();
		BeanUtils.copyProperties(answerService.getAnswer(query), answerView);
		return answerView;
	}

	/**
	 * 校验问卷并且判断是否要更新最近一次的答案
	 * @param project
	 * @return
	 */
	private String validateAndGetLatestAnswer(ProjectView project) {
		ProjectSetting setting = project.getSetting();
		boolean needGetLatest = false;
		try {
			validateProject(project);
			// 未设时间限制&需要登录&可以修改，永远修改的是同一份
			if (SecurityContextUtils.isAuthenticated() && setting != null
					&& Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
				needGetLatest = true;
			}
		}
		catch (ErrorCodeException e) {
			// 如果设置了时间限制，只能修改某个时间区间内的问卷
			// 登录&问卷已提交&允许修改，则可以继续修改
			if (ErrorCode.SurveySubmitted.equals(e.getErrorCode()) && SecurityContextUtils.isAuthenticated()
					&& setting != null && Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
				needGetLatest = true;
			}
			else {
				throw e;
			}
		}
		// 获取最近一份的问卷执行答案更新操作
		if (needGetLatest) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(project.getId());
			answerQuery.setLatest(true);
			AnswerView latestAnswer = answerService.getAnswer(answerQuery);
			if (latestAnswer != null) {
				return answerQuery.getId();
			}
		}
		return null;
	}

	private void validateLoginLimit(String projectId, ProjectSetting setting) {
		String userId = SecurityContextUtils.getUserId();
		if (userId == null) {
			log.info("user is empty");
			return;
		}
		ProjectSetting.UniqueLimitSetting loginLimitSetting = setting.getAnswerSetting().getLoginLimit();
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		query.setCreateBy(userId);
		doValidate(loginLimitSetting, query);
	}

	private void validateCookieLimit(String projectId, ProjectSetting setting) {
		HttpServletRequest request = ContextHelper.getCurrentHttpRequest();

		Cookie limitCookie = WebUtils.getCookie(request, AppConsts.COOKIE_LIMIT_NAME);
		if (limitCookie == null) {
			// 添加 cookie
			HttpServletResponse response = ContextHelper.getCurrentHttpResponse();

			final Cookie cookie = new Cookie(AppConsts.COOKIE_LIMIT_NAME, UUID.randomUUID().toString());
			cookie.setPath("/");
			cookie.setMaxAge(100 * 360 * 24 * 60 * 60);
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			response.addCookie(cookie);
			return;
		}
		ProjectSetting.UniqueLimitSetting uniqueLimitSetting = setting.getAnswerSetting().getCookieLimit();
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		query.setCookie(limitCookie.getValue());
		doValidate(uniqueLimitSetting, query);
	}

	private void validateIpLimit(String projectId, ProjectSetting setting) {
		HttpServletRequest request = ContextHelper.getCurrentHttpRequest();
		String ip = IPUtils.getClientIpAddress(request);
		if (ip == null) {
			log.info("ip is empty");
			return;
		}
		ProjectSetting.UniqueLimitSetting ipLimitSetting = setting.getAnswerSetting().getIpLimit();
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		query.setIp(ip);
		doValidate(ipLimitSetting, query);
	}

	private void doValidate(ProjectSetting.UniqueLimitSetting setting, AnswerQuery query) {
		// 通过 cron 计算时间窗
		CronHelper helper = new CronHelper(setting.getLimitFreq().getCron());
		Tuple2<LocalDateTime, LocalDateTime> currentWindow = helper.currentWindow();
		if (currentWindow != null) {
			query.setStartTime(Date.from(currentWindow.getFirst().atZone(ZoneId.systemDefault()).toInstant()));
			query.setEndTime(Date.from(currentWindow.getSecond().atZone(ZoneId.systemDefault()).toInstant()));
		}
		long total = answerService.count(query);
		if (setting.getLimitNum() != null && total >= setting.getLimitNum()) {
			throw new ErrorCodeException(ErrorCode.SurveySubmitted);
		}
	}

}
