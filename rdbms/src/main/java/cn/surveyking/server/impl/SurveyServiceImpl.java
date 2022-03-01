package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.Tuple2;
import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.core.uitls.CronHelper;
import cn.surveyking.server.core.uitls.IPUtils;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.AnswerQuery;
import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.domain.model.Project;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
		if (setting.getStatus() == 0) {
			throw new ErrorCodeException(ErrorCode.SurveySuspend);
		}

		Long maxAnswers = setting.getAnswerSetting().getMaxAnswers();
		// 校验最大答案条数限制
		if (maxAnswers != null) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(projectId);
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
		query.setStartTime(Date.from(currentWindow.getFirst().atZone(ZoneId.systemDefault()).toInstant()));
		query.setEndTime(Date.from(currentWindow.getSecond().atZone(ZoneId.systemDefault()).toInstant()));
		long total = answerService.count(query);
		if (setting.getLimitNum() != null && total >= setting.getLimitNum()) {
			throw new ErrorCodeException(ErrorCode.SurveySubmitted);
		}
	}

}
