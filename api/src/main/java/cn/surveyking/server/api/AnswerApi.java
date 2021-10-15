package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.AnswerQuery;
import cn.surveyking.server.domain.dto.AnswerRequest;
import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.domain.dto.DownloadQuery;
import cn.surveyking.server.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("${api.prefix}/answers")
@RequiredArgsConstructor
public class AnswerApi {

	private final AnswerService answerService;

	@PreAuthorize("hasAuthority('answer:list')")
	@GetMapping
	public PaginationResponse<AnswerView> listAnswer(AnswerQuery query) {
		return answerService.listAnswer(query);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('answer:detail')")
	public AnswerView getAnswer(@PathVariable String id, AnswerQuery query) {
		query.setId(id);
		return answerService.getAnswer(query);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('answer:create')")
	public void saveAnswer(@RequestBody AnswerRequest answer, HttpServletRequest request) {
		answerService.saveAnswer(answer, request);
	}

	@PatchMapping
	@PreAuthorize("hasAuthority('answer:update')")
	public void updateAnswer(@RequestBody AnswerRequest answer) {
		answerService.updateAnswer(answer);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('answer:delete')")
	public void deleteAnswer(String[] ids) {
		answerService.deleteAnswer(ids);
	}

	@GetMapping("/download")
	@PreAuthorize("hasAuthority('answer:export')")
	public ResponseEntity<Resource> download(DownloadQuery query) {
		return answerService.download(query);
	}

}
