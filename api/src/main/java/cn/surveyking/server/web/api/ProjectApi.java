package cn.surveyking.server.web.api;

import cn.surveyking.server.web.domain.dto.ProjectQuery;
import cn.surveyking.server.web.domain.dto.ProjectView;
import cn.surveyking.server.web.domain.model.Project;
import cn.surveyking.server.web.domain.model.ProjectSetting;
import cn.surveyking.server.web.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectApi {

	private final ProjectService projectService;

	@GetMapping
	public List<ProjectView> listProject(ProjectQuery query) {
		return projectService.listProject(query);
	}

	@GetMapping("/{shortId}")
	public ProjectView getProject(@PathVariable String shortId, ProjectQuery query) {
		query.setShortId(shortId);
		return projectService.getProject(query);
	}

	@GetMapping("/{shortId}/settings")
	public ProjectSetting getSetting(@PathVariable String shortId, ProjectQuery query) {
		query.setShortId(shortId);
		return projectService.getSetting(query);
	}

	@PostMapping
	public String addProject(@RequestBody Project project) {
		return projectService.addProject(project);
	}

	@PatchMapping
	public void updateProject(@RequestBody Project project) {
		projectService.updateProject(project);
	}

	@DeleteMapping("{id}")
	public void deleteProject(@PathVariable String id) {
		projectService.deleteProject(id);
	}

}
