package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 问卷/问题模板
 *
 * @author javahuang
 * @date 2021/9/23
 */
@RestController
@RequestMapping("${api.prefix}/templates")
@RequiredArgsConstructor
public class TemplateApi {

	private final TemplateService templateService;

	@GetMapping
	@PreAuthorize("hasAuthority('template:list')")
	public PaginationResponse<TemplateView> listQuestionTemplate(TemplateQuery query) {
		return templateService.listTemplate(query);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('template:create')")
	public String addTemplate(@RequestBody TemplateRequest template) {
		return templateService.addTemplate(template);
	}

	@PatchMapping
	@PreAuthorize("hasAuthority('template:update')")
	public void updateTemplate(@RequestBody TemplateRequest template) {
		templateService.updateTemplate(template);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('template:delete')")
	public void deleteTemplate(String[] ids) {
		templateService.deleteTemplate(Arrays.asList(ids));
	}

	@GetMapping("/getCategories")
	public List<String> listTemplateCategories(CategoryQuery query) {
		return templateService.listTemplateCategories(query);
	}

	@GetMapping("/getTags")
	public Set<String> getTags(TagQuery query) {
		return templateService.getTags(query);
	}

}
