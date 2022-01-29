package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.ProjectPartnerService;
import cn.surveyking.server.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("${api.prefix}/projects")
@RequiredArgsConstructor
public class ProjectApi {

	private final ProjectService projectService;

	private final ProjectPartnerService projectPartnerService;

	@GetMapping
	@PreAuthorize("hasAuthority('project:list')")
	public PaginationResponse<ProjectView> listProject(ProjectQuery query) {
		return projectService.listProject(query);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('project:detail')")
	public ProjectView getProject(@PathVariable String id, ProjectQuery query) {
		query.setId(id);
		return projectService.getProject(query);
	}

	@GetMapping("/{id}/settings")
	@PreAuthorize("hasAuthority('project:detail')")
	public ProjectSetting getSetting(@PathVariable String id, ProjectQuery query) {
		query.setId(id);
		return projectService.getSetting(query);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('project:create')")
	public ProjectView addProject(@RequestBody ProjectRequest project) {
		return projectService.addProject(project);
	}

	@PatchMapping
	@PreAuthorize("hasAuthority('project:update')")
	public void updateProject(@RequestBody ProjectRequest project) {
		projectService.updateProject(project);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('project:delete')")
	public void deleteProject(@PathVariable String id) {
		projectService.deleteProject(id);
	}

	@GetMapping("/listPartner")
	public List<ProjectPartnerView> listProjectPartner(String projectId) {
		return projectPartnerService.listProjectPartner(projectId);
	}

	@PostMapping("/addPartner")
	public void addProjectPartner(@RequestBody ProjectPartnerRequest request) {
		projectPartnerService.addProjectPartner(request);
	}

	@DeleteMapping("/deletePartner")
	public void deleteProjectPartner(String id) {
		projectPartnerService.deleteProjectPartner(id);
	}

}
