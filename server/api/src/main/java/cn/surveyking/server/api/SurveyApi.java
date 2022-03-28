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
		ProjectSetting setting = surveyService.validateProject(projectId);
		String answerId = answerService.saveAnswer(answer, request);

		if (!StringUtils.hasText(answer.getId())) {
			ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest();
			approvalTaskRequest.setType(FlowApprovalType.SAVE);
			approvalTaskRequest.setAnswerId(answerId);
			approvalTaskRequest.setProjectId(projectId);
			approvalTaskRequest.setActivityId(answer.getProjectId());
			flowService.approvalTask(approvalTaskRequest);
		}

		if (Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
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
