package cn.surveyking.server.api;

import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.ProjectView;
import cn.surveyking.server.service.ProjectService;
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
	public String addProject(@RequestBody ProjectRequest project) {
		return projectService.addProject(project);
	}

	@PatchMapping
	public void updateProject(@RequestBody ProjectRequest project) {
		projectService.updateProject(project);
	}

	@DeleteMapping("{id}")
	public void deleteProject(@PathVariable String id) {
		projectService.deleteProject(id);
	}

}
