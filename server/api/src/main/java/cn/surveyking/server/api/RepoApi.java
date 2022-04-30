package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.RepoQuery;
import cn.surveyking.server.domain.dto.RepoRequest;
import cn.surveyking.server.domain.dto.RepoTemplateRequest;
import cn.surveyking.server.domain.dto.RepoView;
import cn.surveyking.server.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@RequestMapping("${api.prefix}/repos")
@RequiredArgsConstructor
@RestController
public class RepoApi {

	private final RepoService repoService;

	@GetMapping
	@PreAuthorize("hasAuthority('repo:list')")
	public PaginationResponse<RepoView> listRepo(RepoQuery query) {
		return repoService.listRepo(query);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('repo:detail')")
	public RepoView getRpo(@PathVariable String id) {
		return repoService.getRpo(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('repo:create')")
	public void addRepo(@RequestBody RepoRequest request) {
		repoService.addRepo(request);
	}

	@PatchMapping
	@PreAuthorize("hasAuthority('repo:update')")
	public void updateRepo(@RequestBody RepoRequest request) {
		repoService.updateRepo(request);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('repo:delete')")
	public void deleteRepo(@PathVariable String id) {
		repoService.deleteRepo(id);
	}

	@PostMapping("/batch")
	public void batchAddRepoTemplate(@RequestBody RepoTemplateRequest request) {
		repoService.batchAddRepoTemplate(request);
	}

	@PostMapping("/unbind")
	public void batchUnBindTemplate(@RequestBody RepoTemplateRequest request) {
		repoService.batchUnBindTemplate(request);
	}

}
