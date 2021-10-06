package cn.surveyking.server.api;

import cn.surveyking.server.core.pagination.PaginationResponse;
import cn.surveyking.server.domain.dto.AnswerQuery;
import cn.surveyking.server.domain.dto.AnswerRequest;
import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.domain.dto.DownloadQuery;
import cn.surveyking.server.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerApi {

	private final AnswerService answerService;

	@GetMapping
	public PaginationResponse<AnswerView> listAnswer(AnswerQuery query) {
		return answerService.listAnswer(query);
	}

	@GetMapping("/{id}")
	public AnswerView getAnswer(@PathVariable String id, AnswerQuery query) {
		query.setId(id);
		return answerService.getAnswer(query);
	}

	@PostMapping
	public void saveAnswer(@RequestBody AnswerRequest answer, HttpServletRequest request) {
		answerService.saveAnswer(answer, request);
	}

	@PatchMapping
	public void updateAnswer(@RequestBody AnswerRequest answer) {
		answerService.updateAnswer(answer);
	}

	@DeleteMapping
	public void deleteAnswer(String[] ids) {
		answerService.deleteAnswer(ids);
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> download(DownloadQuery query) {
		return answerService.download(query);
	}

}
