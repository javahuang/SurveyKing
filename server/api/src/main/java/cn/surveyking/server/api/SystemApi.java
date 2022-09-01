package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@RestController
@RequestMapping("${api.prefix}/system")
@RequiredArgsConstructor
public class SystemApi {

	private final SystemService systemService;

	private final UserService userService;

	private final PositionService positionService;

	private final DeptService deptService;

	private final RoleService roleService;

	private final DictService dictService;

	/**
	 * @return 当前系统信息
	 */
	@GetMapping
	public SystemInfo getSystemInfo() {
		return systemService.getSystemInfo();
	}

	/**
	 * 更新系统信息
	 * @param request 更新请求
	 */
	@PostMapping("/update")
	@PreAuthorize("hasRole('admin')")
	public void updateSystemInfo(@RequestBody SystemInfoRequest request) {
		systemService.updateSystemInfo(request);
	}

	/**
	 * 获取系统角色列表
	 * @param query 角色查询请求
	 * @return
	 */
	@RequestMapping("/role/list")
	@PreAuthorize("hasAuthority('system:role:list')")
	public PaginationResponse<RoleView> roles(RoleQuery query) {
		return systemService.getRoles(query);
	}

	/**
	 * 添加系统角色
	 * @param request 角色信息
	 */
	@PostMapping("/role/create")
	@PreAuthorize("hasAuthority('system:role:create')")
	public void createRole(@RequestBody RoleRequest request) {
		systemService.createRole(request);
	}

	/**
	 * 更新系统角色
	 * @param request 角色信息
	 */
	@PostMapping("/role/update")
	@PreAuthorize("hasAuthority('system:role:update')")
	public void updateRole(@RequestBody RoleRequest request) {
		systemService.updateRole(request);
	}

	/**
	 * 删除系统角色
	 * @param request 角色信息
	 */
	@PostMapping("/role/delete")
	@PreAuthorize("hasAuthority('system:role:delete')")
	public void deleteRole(@RequestBody RoleRequest request) {
		systemService.deleteRole(request);
	}

	/**
	 * 获取系统权限列表
	 * @return 权限列表
	 */
	@RequestMapping("/permission/list")
	public List<PermissionView> permissions() {
		return systemService.getPermissions();
	}

	/**
	 * 比对数据库和代码里面配置的权限
	 */
	@GetMapping("/permission/diff")
	@PreAuthorize("hasRole('admin')")
	public void extractCodeDiffDbPermissions() {
		systemService.extractCodeDiffDbPermissions();
	}

	/**
	 * 系统用户列表
	 * @param query 查询用户信息
	 * @return
	 */
	@RequestMapping("/user/list")
	@PreAuthorize("hasAuthority('system:user:list')")
	public PaginationResponse<UserView> roles(UserQuery query) {
		return userService.getUsers(query);
	}

	/**
	 * 创建系统用户
	 * @param request
	 */
	@PostMapping("/user/create")
	@PreAuthorize("hasAuthority('system:user:create')")
	public void createUser(@RequestBody @Valid UserRequest request) {
		userService.createUser(request);
	}

	/**
	 * 更新系统用户
	 * @param request
	 */
	@PostMapping("/user/update")
	@PreAuthorize("hasAuthority('system:user:update')")
	public void updateUser(@RequestBody @Valid UserRequest request) {
		userService.updateUser(request);
	}

	/**
	 * 更新用户岗位信息
	 * @param request
	 */
	@PostMapping("/user/updatePosition")
	@PreAuthorize("hasAuthority('system:user:updatePosition')")
	public void updateUserPosition(@RequestBody @Valid UserRequest request) {
		userService.updateUserPosition(request);
	}

	/**
	 * 删除用户
	 * @param request
	 */
	@PostMapping("/user/delete")
	@PreAuthorize("hasAuthority('system:user:delete')")
	public void deleteUser(@RequestBody UserRequest request) {
		userService.deleteUser(request.getId());
	}

	/**
	 * 检查登录名是否存在
	 * @param username 登录用户名
	 * @return
	 */
	@GetMapping("/checkUsernameExist")
	public boolean checkUsernameExist(String username) {
		return userService.checkUsernameExist(username);
	}

	/**
	 * 查询岗位列表
	 * @param query
	 * @return
	 */
	@GetMapping("/position/list")
	@PreAuthorize("hasAuthority('system:position:list')")
	public PaginationResponse<PositionView> listPosition(PositionQuery query) {
		return positionService.listPosition(query);
	}

	/**
	 * 添加岗位
	 * @param request
	 */
	@PostMapping("/position/create")
	@PreAuthorize("hasAuthority('system:position:create')")
	public void addPosition(@RequestBody PositionRequest request) {
		positionService.addPosition(request);
	}

	/**
	 * 更新岗位信息
	 * @param request
	 */
	@PostMapping("/position/update")
	@PreAuthorize("hasAuthority('system:position:update')")
	public void updatePosition(@RequestBody PositionRequest request) {
		positionService.updatePosition(request);
	}

	/**
	 * 删除岗位信息
	 * @param request
	 */
	@PostMapping("/position/delete")
	@PreAuthorize("hasAuthority('system:position:delete')")
	public void deletePosition(@RequestBody PositionRequest request) {
		positionService.deletePosition(request.getId());
	}

	/**
	 * 获取部门列表
	 * @return
	 */
	@GetMapping("/dept/list")
	@PreAuthorize("hasAuthority('system:dept:list')")
	public List<DeptView> listDept() {
		return deptService.listDept(null);
	}

	@PostMapping("/dept/create")
	@PreAuthorize("hasAuthority('system:dept:create')")
	public void addOrg(@RequestBody DeptRequest request) {
		deptService.addDept(request);
	}

	@PostMapping("/dept/update")
	@PreAuthorize("hasAuthority('system:dept:update')")
	public void updateOrg(@RequestBody DeptRequest request) {
		deptService.updateDept(request);
	}

	@PostMapping("/dept/delete")
	@PreAuthorize("hasAuthority('system:dept:delete')")
	public void deleteOrg(@RequestBody DeptRequest request) {
		deptService.deleteDept(request.getId());
	}

	@PostMapping("/dept/sort")
	@PreAuthorize("hasAuthority('system:dept:create')")
	public void sortOrg(@RequestBody DeptSortRequest request) {
		deptService.sortDept(request);
	}

	/**
	 * 获取字典项列表
	 * @param query 字典项分页参数
	 * @return
	 */
	@GetMapping("/dict/list")
	@PreAuthorize("hasAuthority('system:dict:list')")
	public PaginationResponse<CommDictView> listDict(CommDictQuery query) {
		return dictService.listDict(query);
	}

	/**
	 * 创建字典项
	 * @param request
	 */
	@PostMapping("/dict/create")
	@PreAuthorize("hasAuthority('system:dict:create')")
	public void addDict(@RequestBody CommDictRequest request) {
		dictService.addDict(request);
	}

	/**
	 * 更新字典项
	 * @param request
	 */
	@PostMapping("/dict/update")
	@PreAuthorize("hasAuthority('system:dict:update')")
	public void updateDict(@RequestBody CommDictRequest request) {
		dictService.updateDict(request);
	}

	/**
	 * 删除字典项
	 * @param request
	 */
	@PostMapping("/dict/delete")
	@PreAuthorize("hasAuthority('system:dict:delete')")
	public void deleteDict(@RequestBody CommDictRequest request) {
		dictService.deleteDict(request.getId());
	}

	/**
	 * 获取字典条目列表
	 * @param query
	 * @return
	 */
	@GetMapping("/dictItem/list")
	@PreAuthorize("hasAuthority('system:dictItem:list')")
	public PaginationResponse<CommDictItemView> listDictItem(CommDictItemQuery query) {
		return dictService.listDictItem(query);
	}

	/**
	 * 添加或者修改字典条目
	 * @param request
	 */
	@PostMapping("/dictItem/create")
	@PreAuthorize("hasAuthority('system:dictItem:create')")
	public void createDictItem(@RequestBody CommDictItemRequest request) {
		dictService.saveOrUpdateDictItem(request);
	}

	/**
	 * 添加或者修改字典条目
	 * @param request
	 */
	@PostMapping("/dictItem/update")
	@PreAuthorize("hasAuthority('system:dictItem:update')")
	public void updateItem(@RequestBody CommDictItemRequest request) {
		dictService.saveOrUpdateDictItem(request);
	}

	/**
	 * 导入字典条目
	 * @param request
	 */
	@PostMapping("/dictItem/import")
	@PreAuthorize("hasAuthority('system:dictItem:import')")
	public void importDictItem(CommDictItemRequest request) {
		dictService.importDictItem(request);
	}

	/**
	 * 删除字典条目
	 * @param request
	 */
	@PostMapping("/dictItem/delete")
	@PreAuthorize("hasAuthority('system:dictItem:delete')")
	public void deleteDictItem(@RequestBody CommDictItemRequest request) {
		dictService.deleteDictItem(request.getId());
	}

	@PostMapping("/selectUser")
	public List<UserInfo> selectUsers(@RequestBody SelectUserRequest request) {
		return userService.selectUsers(request);
	}

	@PostMapping("/selectDept")
	public List<DeptView> selectDepts(@RequestBody SelectDeptRequest request) {
		return deptService.listDept(request);
	}

	@PostMapping("/selectRole")
	public List<RoleView> selectRoles(@RequestBody SelectRoleRequest request) {
		return roleService.selectRoles(request);
	}

	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/selectPosition")
	public List<PositionView> selectPositions(@RequestBody SelectPositionRequest request) {
		return positionService.selectPositions(request);
	}

}
