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
@RequestMapping("${api.prefix}/project")
@RequiredArgsConstructor
public class ProjectApi {

	private final ProjectService projectService;

	private final ProjectPartnerService projectPartnerService;

	/**
	 * 获取项目列表
	 * @param query 分页查询参数
	 * @return 项目列表分页结果
	 */
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('project:list')")
	public PaginationResponse<ProjectView> listProject(ProjectQuery query) {
		return projectService.listProject(query);
	}

	/**
	 * 获取项目信息
	 * @param id 项目 id
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('project:detail')")
	@EnableDataPerm(key = "#id")
	public ProjectView getProject(String id) {
		return projectService.getProject(id);
	}

	/**
	 * 获取项目设置
	 * @param query
	 * @return
	 */
	@GetMapping("/setting")
	@PreAuthorize("hasAuthority('project:detail')")
	@EnableDataPerm(key = "#id")
	public ProjectSetting getSetting(ProjectQuery query) {
		return projectService.getSetting(query);
	}

	/**
	 * 添加项目
	 * @param project
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('project:create')")
	public ProjectView addProject(@RequestBody ProjectRequest project) {
		return projectService.addProject(project);
	}

	/**
	 * 更新项目
	 * @param project
	 */
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('project:update')")
	@EnableDataPerm(key = "#project.id")
	public void updateProject(@RequestBody ProjectRequest project) {
		projectService.updateProject(project);
	}

	/**
	 * 删除项目
	 * @param project
	 */
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('project:delete')")
	@EnableDataPerm(key = "#project.id")
	public void deleteProject(@RequestBody ProjectRequest project) {
		projectService.deleteProject(project);
	}

	/**
	 * 获取项目参与者列表
	 * @param query
	 * @return
	 */
	@GetMapping("/partner/list")
	@EnableDataPerm(key = "#query.projectId")
	public List<ProjectPartnerView> listProjectPartner(ProjectPartnerQuery query) {
		return projectPartnerService.listProjectPartner(query);
	}

	/**
	 * 添加项目参与者
	 * @param request
	 */
	@PostMapping("/partner/create")
	@EnableDataPerm(key = "#request.projectId")
	public void addProjectPartner(@RequestBody ProjectPartnerRequest request) {
		projectPartnerService.addProjectPartner(request);
	}

	/**
	 * 删除项目参与者
	 * @param request
	 */
	@PostMapping("/partner/delete")
	@EnableDataPerm(key = "#request.projectId")
	public void deleteProjectPartner(@RequestBody ProjectPartnerRequest request) {
		projectPartnerService.deleteProjectPartner(request);
	}

	/**
	 * 获取回收站里的项目列表
	 * @param query
	 * @return
	 */
	@GetMapping("/trash")
	@PreAuthorize("hasAuthority('project:list')")
	public List<ProjectView> getDeleted(ProjectQuery query) {
		return projectService.getDeleted(query);
	}

	/**
	 * 从回收站彻底移除项目
	 * @param request
	 */
	@PostMapping("/destroy")
	@PreAuthorize("hasAuthority('project:create')")
	public void batchDestroyProject(@RequestBody ProjectRequest request) {
		projectService.batchDestroyProject(request);
	}

	/**
	 * 从回收站里面恢复项目
	 * @param request
	 */
	@PostMapping("/restore")
	@PreAuthorize("hasAuthority('project:create')")
	public void restoreProject(@RequestBody ProjectRequest request) {
		projectService.restoreProject(request);
	}

}
