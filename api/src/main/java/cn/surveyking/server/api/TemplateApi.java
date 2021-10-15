package cn.surveyking.server.api;

import cn.surveyking.server.domain.dto.TemplateQuery;
import cn.surveyking.server.domain.dto.TemplateRequest;
import cn.surveyking.server.domain.dto.TemplateView;
import cn.surveyking.server.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
	@PreAuthorize("hasAuthority('template')")
	public List<TemplateView> listQuestionTemplate(TemplateQuery query) {
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
	public void deleteTemplate(String id) {
		templateService.deleteTemplate(id);
	}

	@GetMapping("/getCategories")
	@PreAuthorize("hasAuthority('template:category')")
	public List<String> listTemplateCategories(String search) {
		return templateService.listTemplateCategories(search);
	}

	@GetMapping("/getTags")
	@PreAuthorize("hasAuthority('template:tag')")
	public Set<String> listTemplateTags(String search) {
		return templateService.listTemplateTags(search);
	}

}
