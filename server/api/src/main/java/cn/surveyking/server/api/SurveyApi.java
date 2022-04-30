package cn.surveyking.server.api;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.flow.service.FlowService;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
		PublicProjectView projectView = surveyService.loadProject(query.getId());
		if (projectView.getSurvey() != null) {
			flowService.beforeLaunchProcess(projectView);
		}
		return projectView;
	}

	@PostMapping("/loadAnswer")
	public PublicAnswerView loadAnswer(@RequestBody AnswerQuery query) {
		return surveyService.loadAnswer(query);
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
	public PublicAnswerView saveAnswer(@RequestBody AnswerRequest answer, HttpServletRequest request) {
		PublicAnswerView publicAnswerView = surveyService.saveAnswer(answer, request);
		// // 发起审批流程
		// if (!StringUtils.hasText(answer.getId())) {
		// ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest();
		// approvalTaskRequest.setType(FlowApprovalType.SAVE);
		// approvalTaskRequest.setAnswerId(publicAnswerView.getAnswerId());
		// approvalTaskRequest.setProjectId(answer.getProjectId());
		// approvalTaskRequest.setActivityId(answer.getProjectId());
		// flowService.approvalTask(approvalTaskRequest);
		// }
		return publicAnswerView;
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
