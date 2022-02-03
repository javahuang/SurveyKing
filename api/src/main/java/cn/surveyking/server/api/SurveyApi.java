package cn.surveyking.server.api;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.flow.constant.FlowApprovalType;
import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import cn.surveyking.server.flow.service.FlowService;
import cn.surveyking.server.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;

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

	private final UserService userService;

	private final DeptService deptService;

	@GetMapping("/loadProject")
	public PublicProjectView loadProject(ProjectQuery query) {
		PublicProjectView projectView = surveyService.loadProject(query);
		SurveySchema schema = flowService.beforeLaunchProcess(SecurityContextUtils.getUserId(),
				projectView.getSurvey());
		projectView.setSurvey(schema);
		return projectView;
	}

	@PostMapping("/verifyPassword")
	public PublicProjectView verifyPassword(@RequestBody ProjectQuery query) {
		PublicProjectView projectView = surveyService.verifyPassword(query);
		SurveySchema schema = flowService.beforeLaunchProcess(SecurityContextUtils.getUserId(),
				projectView.getSurvey());
		projectView.setSurvey(schema);
		return projectView;
	}

	@PostMapping("/saveAnswer")
	public void saveAnswer(@RequestBody AnswerRequest answer, HttpServletRequest request) {
		String answerId = answerService.saveAnswer(answer, request);
		ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest();
		approvalTaskRequest.setType(FlowApprovalType.SAVE);
		approvalTaskRequest.setAnswerId(answerId);
		approvalTaskRequest.setProjectId(answer.getProjectId());
		approvalTaskRequest.setActivityId(answer.getProjectId());
		flowService.approvalTask(approvalTaskRequest);
	}

	@PostMapping("/upload")
	public FileView upload(@RequestParam("file") MultipartFile file) {
		return fileService.upload(file, AppConsts.StorageType.ANSWER_ATTACHMENT);
	}

	@GetMapping("/preview/{attachmentId}")
	public ResponseEntity<Resource> preview(@PathVariable String attachmentId) {
		Resource file = fileService.loadAsResource(attachmentId);
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(Duration.ofDays(30)))
				.contentType(MediaType.IMAGE_JPEG).body(file);
	}

	@PostMapping("/selectUsers")
	public List<UserInfo> selectUsers(@RequestBody SelectUserQuery query) {
		return userService.selectUsers(query);
	}

	@PostMapping("/selectDepts")
	public List<DeptView> selectDepts(@RequestBody SelectDeptQuery query) {
		return deptService.listDept();
	}

}
