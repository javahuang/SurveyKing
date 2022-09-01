package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@RequestMapping("${api.prefix}/repo")
@RequiredArgsConstructor
@RestController
public class RepoApi {

	private final RepoService repoService;

	/**
	 * @param query
	 * @return
	 */
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('repo:list')")
	public PaginationResponse<RepoView> listRepo(RepoQuery query) {
		return repoService.listRepo(query);
	}

	@PreAuthorize("hasAuthority('repo:detail')")
	public RepoView getRpo(String id) {
		return repoService.getRpo(id);
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('repo:create')")
	public void addRepo(@RequestBody RepoRequest request) {
		repoService.addRepo(request);
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('repo:update')")
	public void updateRepo(@RequestBody RepoRequest request) {
		repoService.updateRepo(request);
	}

	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('repo:delete')")
	public void deleteRepo(RepoRequest request) {
		repoService.deleteRepo(request);
	}

	@PostMapping("/batchCreate")
	public void batchAddRepoTemplate(@RequestBody RepoTemplateRequest request) {
		repoService.batchAddRepoTemplate(request);
	}

	@PostMapping("/unbind")
	public void batchUnBindTemplate(@RequestBody RepoTemplateRequest request) {
		repoService.batchUnBindTemplate(request);
	}

	/**
	 * 从题库里面挑选试题
	 * @param repos
	 * @return
	 */
	@PostMapping("/pick")
	public List<SurveySchema> pickQuestionFromRepo(@RequestBody List<ProjectSetting.RandomSurveyCondition> repos) {
		return repoService.pickQuestionFromRepo(repos);
	}

	@PostMapping("/import")
	public void importFromTemplate(RepoTemplateRequest request) {
		repoService.importFromTemplate(request);
	}

}
