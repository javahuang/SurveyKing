package cn.surveyking.server.api;

import cn.surveyking.server.core.annotation.EnableDataPerm;
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
	@EnableDataPerm(key = "#id")
	public ProjectView getProject(@PathVariable String id) {
		return projectService.getProject(id);
	}

	@GetMapping("/{id}/settings")
	@PreAuthorize("hasAuthority('project:detail')")
	@EnableDataPerm(key = "#id")
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
	@EnableDataPerm(key = "#project.id")
	public void updateProject(@RequestBody ProjectRequest project) {
		projectService.updateProject(project);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('project:delete')")
	@EnableDataPerm(key = "#id")
	public void deleteProject(@PathVariable String id) {
		projectService.deleteProject(id);
	}

	@GetMapping("/listPartner")
	@EnableDataPerm(key = "#projectId")
	public List<ProjectPartnerView> listProjectPartner(String projectId) {
		return projectPartnerService.listProjectPartner(projectId);
	}

	@PostMapping("/addPartner")
	@EnableDataPerm(key = "#request.projectId")
	public void addProjectPartner(@RequestBody ProjectPartnerRequest request) {
		projectPartnerService.addProjectPartner(request);
	}

	@DeleteMapping("/deletePartner")
	@EnableDataPerm(key = "#projectId")
	public void deleteProjectPartner(String projectId, String id) {
		projectPartnerService.deleteProjectPartner(projectId, id);
	}

}
