package cn.surveyking.server.api.controller;

import cn.surveyking.server.api.domain.dto.FileView;
import cn.surveyking.server.api.domain.dto.ProjectQuery;
import cn.surveyking.server.api.domain.dto.PublicProjectView;
import cn.surveyking.server.api.domain.model.Answer;
import cn.surveyking.server.api.service.AnswerService;
import cn.surveyking.server.api.service.FileService;
import cn.surveyking.server.api.service.ProjectService;
import cn.surveyking.server.api.service.SurveyService;
import cn.surveyking.server.core.constant.AppConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 答卷页面
 *
 * @author javahuang
 * @date 2021/8/22
 */
@RequestMapping("/api/public")
@RequiredArgsConstructor
@RestController
public class SurveyApi {

	private final SurveyService surveyService;

	private final AnswerService answerService;

	private final ProjectService projectService;

	private final FileService fileService;

	@GetMapping("/loadProject")
	public PublicProjectView loadProject(ProjectQuery query) {
		return surveyService.loadProject(query);
	}

	@PostMapping("/verifyPassword")
	public PublicProjectView verifyPassword(@RequestBody ProjectQuery query) {
		return surveyService.verifyPassword(query);
	}

	@PostMapping("/saveAnswer")
	public void saveAnswer(@RequestBody Answer answer, HttpServletRequest request) {
		answerService.saveAnswer(answer, request);
	}

	@PostMapping("/upload")
	public FileView upload(@RequestParam("file") MultipartFile file) {
		return fileService.upload(file, AppConsts.StorageType.ANSWER_ATTACHMENT);
	}

	@GetMapping("/preview/{attachmentId}")
	public ResponseEntity<Resource> preview(@PathVariable String attachmentId) {
		Resource file = fileService.loadAsResource(attachmentId);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
	}

}
