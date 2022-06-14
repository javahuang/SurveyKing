package cn.surveyking.server.api;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	private final FileService fileService;

	@PostMapping("/loadProject")
	public PublicProjectView loadProject(@RequestBody ProjectQuery query) {
		return surveyService.loadProject(query);
	}

	@PostMapping("/validateProject")
	public PublicProjectView validateProject(@RequestBody ProjectQuery query) {
		return surveyService.validateProject(query);
	}

	@PostMapping("/statistics")
	public PublicStatisticsView statProject(@RequestBody ProjectQuery query) {
		return surveyService.statProject(query);
	}

	@PostMapping("/saveAnswer")
	public PublicAnswerView saveAnswer(@RequestBody AnswerRequest answer, HttpServletRequest request) {
		PublicAnswerView publicAnswerView = surveyService.saveAnswer(answer, request);
		return publicAnswerView;
	}

	@PostMapping("/upload")
	public FileView upload(UploadFileRequest request) {
		return fileService.upload(request);
	}

	@GetMapping("/preview/{attachmentId}")
	public ResponseEntity<Resource> preview(@PathVariable String attachmentId) {
		FileQuery query = new FileQuery(attachmentId);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(Duration.ofDays(30)).getHeaderValue());
		query.setHeaders(headers);
		return fileService.loadFile(query);
	}

	/**
	 * @param request
	 * @return 公开查询验证页面视图数据
	 */
	@PostMapping("/loadQuery")
	public PublicQueryVerifyView loadQuery(@RequestBody PublicQueryRequest request) {
		return surveyService.loadQuery(request);
	}

	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/getQueryResult")
	public PublicQueryView getQueryResult(@RequestBody PublicQueryRequest request) {
		return surveyService.getQueryResult(request);
	}

}
