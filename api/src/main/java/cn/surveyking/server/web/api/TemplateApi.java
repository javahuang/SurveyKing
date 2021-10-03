package cn.surveyking.server.web.api;

import cn.surveyking.server.web.domain.dto.TemplateQuery;
import cn.surveyking.server.web.domain.dto.TemplateView;
import cn.surveyking.server.web.domain.model.Template;
import cn.surveyking.server.web.service.TemplateService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateApi {

	private final TemplateService templateService;

	@GetMapping
	public List<TemplateView> listQuestionTemplate(TemplateQuery query) {
		return templateService.listTemplate(query);
	}

	@PostMapping
	public String addTemplate(@RequestBody Template template) {
		return templateService.addTemplate(template);
	}

	@PatchMapping
	public void updateTemplate(@RequestBody Template template) {
		templateService.updateTemplate(template);
	}

	@DeleteMapping
	public void deleteTemplate(String id) {
		templateService.deleteTemplate(id);
	}

	@GetMapping("/getCategories")
	public List<String> listTemplateCategories(String search) {
		return templateService.listTemplateCategories(search);
	}

	@GetMapping("/getTags")
	public Set<String> listTemplateTags(String search) {
		return templateService.listTemplateTags(search);
	}

}
