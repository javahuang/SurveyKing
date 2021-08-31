package cn.surveyking.server.api.controller;

import cn.surveyking.server.api.domain.dto.ProjectQuery;
import cn.surveyking.server.api.domain.dto.PublicProjectView;
import cn.surveyking.server.api.domain.model.Answer;
import cn.surveyking.server.api.service.AnswerService;
import cn.surveyking.server.api.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
