package cn.surveyking.server.api;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.flow.constant.FlowApprovalType;
import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import cn.surveyking.server.flow.service.FlowService;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * 答卷页面
 *
 * @author javahuang
 * @date 2021/8/22
 */
@RequestMapping("${api.prefix}/public")
@RequiredArgsConstructor
@RestController
public class SurveyApi {

	private final SurveyService surveyService;

	private final AnswerService answerService;

	private final FileService fileService;

	private final FlowService flowService;

	@PostMapping("/loadProject")
	public PublicProjectView loadProject(@RequestBody ProjectQuery query) {
		surveyService.validateProject(query.getId());
		PublicProjectView projectView = surveyService.loadProject(query.getId());
		if (Boolean.TRUE.equals(projectView.getSetting().getAnswerSetting().getLoginRequired())
				&& !SecurityContextUtils.isAuthenticated()) {
			projectView.setLoginRequired(true);
			projectView.setSurvey(null);
		}
		else {
			flowService.beforeLaunchProcess(projectView);
		}
		if (Boolean.TRUE.equals(projectView.getSetting().getSubmittedSetting().getEnableUpdate())
				&& SecurityContextUtils.isAuthenticated()) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(query.getId());
			answerQuery.setLatest(true);
			AnswerView latestAnswer = answerService.getAnswer(answerQuery);
			if (latestAnswer != null) {
				projectView.setAnswerId(latestAnswer.getId());
			}
		}

		return projectView;
	}

	@PostMapping("/loadAnswer")
	public AnswerView loadAnswer(@RequestBody AnswerQuery query) {
		ProjectSetting setting = null;
		try {
			setting = surveyService.validateProject(query.getProjectId());
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
		return answerService.getAnswer(query);
	}

	@PostMapping("/verifyPassword")
	public PublicProjectView verifyPassword(@RequestBody ProjectQuery query) {
		PublicProjectView projectView = surveyService.verifyPassword(query);
		flowService.beforeLaunchProcess(projectView);
		return projectView;
	}

	@PostMapping("/statistics")
	public PublicStatisticsView statProject(@RequestBody ProjectQuery query) {
		return surveyService.statProject(query);
	}

	@PostMapping("/saveAnswer")
	public String saveAnswer(@RequestBody AnswerRequest answer, HttpServletRequest request) {
		String projectId = answer.getProjectId();
		ProjectSetting setting = null;
		boolean needGetLatest = false;
		try {
			setting = surveyService.validateProject(projectId);

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
		}

		if (needGetLatest) {
			// 获取最近一份的问卷
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(projectId);
			answerQuery.setLatest(true);
			AnswerView latestAnswer = answerService.getAnswer(answerQuery);
			if (latestAnswer != null) {
				answer.setId(latestAnswer.getId());
			}
		}

		String answerId = answerService.saveAnswer(answer, request);

		if (!StringUtils.hasText(answer.getId())) {
			ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest();
			approvalTaskRequest.setType(FlowApprovalType.SAVE);
			approvalTaskRequest.setAnswerId(answerId);
			approvalTaskRequest.setProjectId(projectId);
			approvalTaskRequest.setActivityId(answer.getProjectId());
			flowService.approvalTask(approvalTaskRequest);
		}
		// 登录用户无需显示二维码
		if (Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())
				&& !SecurityContextUtils.isAuthenticated()) {
			return answerId;
		}
		return null;
	}

	@PostMapping("/upload")
	public FileView upload(@RequestParam("file") MultipartFile file) {
		return fileService.upload(file, AppConsts.StorageType.ANSWER_ATTACHMENT);
	}

	@GetMapping("/preview/{attachmentId}")
	public ResponseEntity<Resource> preview(@PathVariable String attachmentId) {
		FileQuery query = new FileQuery(attachmentId);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(Duration.ofDays(30)).getHeaderValue());
		query.setHeaders(headers);
		return fileService.loadFile(query);
	}

}
