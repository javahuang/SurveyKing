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

	private final FileService fileService;

	/**
	 * 加载问卷
	 * @param query
	 * @return
	 */
	@PostMapping("/loadProject")
	public PublicProjectView loadProject(@RequestBody ProjectQuery query) {
		return surveyService.loadProject(query);
	}

	/**
	 * 校验校验
	 * @param query
	 * @return
	 */
	@PostMapping("/validateProject")
	public PublicProjectView validateProject(@RequestBody ProjectQuery query) {
		return surveyService.validateProject(query);
	}

	/**
	 * 单选、多选投票获取统计信息
	 * @param query
	 * @return
	 */
	@PostMapping("/statistics")
	public PublicStatisticsView statProject(@RequestBody ProjectQuery query) {
		return surveyService.statProject(query);
	}

	/**
	 * 答案保存
	 * @param request
	 * @return
	 */
	@PostMapping("/saveAnswer")
	public PublicAnswerView saveAnswer(@RequestBody AnswerRequest request) {
		PublicAnswerView publicAnswerView = surveyService.saveAnswer(request);
		return publicAnswerView;
	}

	/**
	 * 答案暂存，目前仅支持问题随机
	 * @param request
	 * @return
	 */
	@PostMapping("/tempSaveAnswer")
	public void tempSaveAnswer(@RequestBody AnswerRequest request) {
		surveyService.tempSaveAnswer(request);
	}

	/**
	 * 上传文件
	 * @param request
	 * @return
	 */
	@PostMapping("/upload")
	public FileView upload(UploadFileRequest request) {
		return fileService.upload(request);
	}

	/**
	 * 预览文件
	 * @param attachmentId
	 * @return
	 */
	@GetMapping("/preview/{attachmentId}")
	public ResponseEntity<Resource> preview(@PathVariable("attachmentId") String attachmentId) {
		FileQuery query = new FileQuery(attachmentId);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(Duration.ofDays(30)).getHeaderValue());
		query.setDispositionType(AppConsts.DispositionTypeEnum.inline);
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
	 * 获取公开查询结果
	 * @param request
	 * @return
	 */
	@PostMapping("/getQueryResult")
	public PublicQueryView getQueryResult(@RequestBody PublicQueryRequest request) {
		return surveyService.getQueryResult(request);
	}

	/**
	 * 问卷加载字典
	 * @param request
	 * @return
	 */
	@PostMapping("/loadDict")
	public List<PublicDictView> loadDict(@RequestBody PublicDictRequest request) {
		return surveyService.loadDict(request);
	}

	/**
	 * 加载问卷结果
	 * @param request
	 * @return
	 */
	@PostMapping("/loadExamResult")
	public PublicExamResult loadExamResult(@RequestBody PublicExamRequest request) {
		return surveyService.loadExamResult(request);
	}

}
