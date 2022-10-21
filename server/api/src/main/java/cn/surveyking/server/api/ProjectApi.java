package cn.surveyking.server.api;

import cn.surveyking.server.core.annotation.EnableDataPerm;
import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private final UserService userService;

	private final PositionService positionService;

	private final DeptService deptService;

	private final RoleService roleService;

	private final DictService dictService;

	private final TemplateService templateService;

	private final RepoService repoService;

	private final TagService tagService;

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
	public PaginationResponse<ProjectPartnerView> listProjectPartner(ProjectPartnerQuery query) {
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
	 * 下载项目参与者列表
	 * @param query
	 */
	@GetMapping("/partner/download")
	@EnableDataPerm(key = "#query.projectId")
	public void downloadPartner(ProjectPartnerQuery query) {
		projectPartnerService.downloadPartner(query);
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

	/**
	 * 编辑器里面获取用户信息
	 * @param request
	 * @return
	 */
	@PostMapping("/selectUser")
	public List<UserInfo> selectUser(@RequestBody SelectUserRequest request) {
		return userService.selectUsers(request);
	}

	/**
	 * 编辑器里面获取部门信息
	 * @param request
	 * @return
	 */
	@PostMapping("/selectDept")
	public List<DeptView> selectDept(@RequestBody SelectDeptRequest request) {
		return deptService.listDept(request);
	}

	/**
	 * 编辑器里面获取角色信息
	 * @param request
	 * @return
	 */
	@PostMapping("/selectRole")
	public List<RoleView> selectRole(@RequestBody SelectRoleRequest request) {
		return roleService.selectRoles(request);
	}

	/**
	 * 编辑器里面获取岗位信息
	 * @param request
	 * @return
	 */
	@PostMapping("/selectPosition")
	public List<PositionView> selectPosition(@RequestBody SelectPositionRequest request) {
		return positionService.selectPositions(request);
	}

	/**
	 * 编辑器里面获取字典
	 * @return
	 */
	@PostMapping("/selectDict")
	public List<CommDictView> selectDict() {
		return dictService.selectDict();
	}

	/**
	 * 编辑器里面获取题库模板
	 * @param request
	 * @return
	 */
	@PostMapping("/selectTemplate")
	public Map<String, List<TemplateView>> selectTemplate(@RequestBody SelectTemplateRequest request) {
		return templateService.selectTemplate(request);
	}

	/**
	 * 编辑器里面获取题库
	 * @param request
	 * @return
	 */
	@PostMapping("/selectRepo")
	public List<RepoView> selectRepo(@RequestBody SelectRepoRequest request) {
		return repoService.selectRepo(request);
	}

	/**
	 * 编辑器里面获取标签
	 * @param request
	 * @return
	 */
	@PostMapping("/selectTag")
	public Set<String> selectTag(@RequestBody SelectTagRequest request) {
		return tagService.selectTag(request);
	}

	/**
	 * 导入白名单
	 * @param request
	 */
	@PostMapping("/importProjectPartner")
	public List<UserInfo> importProjectPartner(WhiteListRequest request) {
		return userService.importProjectPartner(request);
	}
}
