package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("${api.prefix}/answer")
@RequiredArgsConstructor
public class AnswerApi {

	private final AnswerService answerService;

	/**
	 * 获取答案列表。
	 * @param query 查询参数。
	 * @return 当前答案。
	 */
	@PreAuthorize("hasAuthority('answer:list')")
	@GetMapping("/list")
	public PaginationResponse<AnswerView> listAnswer(AnswerQuery query) {
		return answerService.listAnswer(query);
	}

	/**
	 * 获取删除的答案。
	 * @param query 查询参数。
	 * @return
	 */
	@PreAuthorize("hasAuthority('answer:list')")
	@GetMapping("/trash")
	public List<AnswerView> listAnswerDeleted(AnswerQuery query) {
		return answerService.listAnswerDeleted(query);
	}

	/**
	 * 获取答案
	 * @param query 查询结果。
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('answer:detail')")
	public AnswerView getAnswer(AnswerQuery query) {
		return answerService.getAnswer(query);
	}

	/**
	 * 数据表格保存答案。
	 * @param request 保存的答案。
	 * @param request
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('answer:create')")
	public void saveAnswer(@RequestBody AnswerRequest request) {
		answerService.saveAnswer(request);
	}

	/**
	 * 更新答案
	 * @param request
	 */
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('answer:update')")
	public void updateAnswer(@RequestBody AnswerRequest request) {
		answerService.updateAnswer(request);
	}

	/**
	 * 删除答案，答案放到回收站
	 * @param request 请求参数
	 */
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('answer:delete')")
	public void deleteAnswer(@RequestBody AnswerRequest request) {
		answerService.deleteAnswer(request);
	}

	/**
	 * 从回收站里面清空答案
	 * @param request
	 */
	@PreAuthorize("hasAuthority('answer:delete')")
	@PostMapping("/destroy")
	public void batchDestroyAnswer(@RequestBody AnswerRequest request) {
		answerService.batchDestroyAnswer(request);
	}

	/**
	 * 从回收站里面恢复答案
	 * @param request
	 */
	@PreAuthorize("hasAuthority('answer:update')")
	@PostMapping("/restore")
	public void restoreAnswer(@RequestBody AnswerRequest request) {
		answerService.restoreAnswer(request);
	}

	/**
	 * 答案导出
	 * @param query 答案导出查询请求
	 * @return
	 */
	@GetMapping("/download")
	@PreAuthorize("hasAuthority('answer:export')")
	public ResponseEntity<Resource> download(DownloadQuery query) {
		return answerService.download(query);
	}

	/**
	 * 修改答案上传附件
	 * @param request 附件
	 * @return 项目 schema
	 */
	@PostMapping("/upload")
	@PreAuthorize("hasAuthority('answer:upload')")
	public AnswerUploadView upload(AnswerUploadRequest request) {
		return answerService.upload(request);
	}

}
