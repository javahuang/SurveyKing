package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.OrgService;
import cn.surveyking.server.service.PositionService;
import cn.surveyking.server.service.SystemService;
import cn.surveyking.server.service.UserService;
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

	private final OrgService orgService;

	@RequestMapping("/roles")
	@PreAuthorize("hasAuthority('system:role:list')")
	public PaginationResponse<RoleView> roles(RoleQuery query) {
		return systemService.getRoles(query);
	}

	@PostMapping("/roles")
	@PreAuthorize("hasAuthority('system:role:create')")
	public void createRole(@RequestBody RoleRequest request) {
		systemService.createRole(request);
	}

	@PatchMapping("/roles/{id}")
	@PreAuthorize("hasAuthority('system:role:update')")
	public void updateRole(@PathVariable("id") String id, @RequestBody RoleRequest request) {
		request.setId(id);
		systemService.updateRole(request);
	}

	@DeleteMapping("/roles/{id}")
	@PreAuthorize("hasAuthority('system:role:delete')")
	public void deleteRole(@PathVariable("id") String id) {
		systemService.deleteRole(id);
	}

	@RequestMapping("/permissions")
	public List<PermissionView> permissions() {
		return systemService.getPermissions();
	}

	/**
	 * 比对数据库和代码里面配置的权限
	 */
	@GetMapping("/permissions/diff")
	@PreAuthorize("hasRole('admin')")
	public void extractCodeDiffDbPermissions() {
		systemService.extractCodeDiffDbPermissions();
	}

	@RequestMapping("/users")
	@PreAuthorize("hasAuthority('system:user:list')")
	public PaginationResponse<UserView> roles(UserQuery query) {
		return userService.getUsers(query);
	}

	@PostMapping("/users")
	@PreAuthorize("hasAuthority('system:user:create')")
	public void createUser(@RequestBody @Valid UserRequest request) {
		userService.createUser(request);
	}

	@PatchMapping("/users/{id}")
	@PreAuthorize("hasAuthority('system:user:update')")
	public void updateUser(@PathVariable("id") String id, @RequestBody @Valid UserRequest request) {
		request.setId(id);
		userService.updateUser(request);
	}

	@PostMapping("/users/{id}/updateUserPosition")
	@PreAuthorize("hasAuthority('system:user:updatePosition')")
	public void updateUserPosition(@PathVariable("id") String id, @RequestBody @Valid UserRequest request) {
		request.setId(id);
		userService.updateUserPosition(request);
	}

	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasAuthority('system:user:delete')")
	public void deleteUser(@PathVariable("id") String id) {
		userService.deleteUser(id);
	}

	@GetMapping("/checkUsernameExist")
	public boolean checkUsernameExist(String username) {
		return userService.checkUsernameExist(username);
	}

	@GetMapping("/positions")
	@PreAuthorize("hasAuthority('system:position:list')")
	public PaginationResponse<PositionView> listPosition(PositionQuery query) {
		return positionService.listPosition(query);
	}

	@PostMapping("/positions")
	@PreAuthorize("hasAuthority('system:position:create')")
	public void addPosition(@RequestBody PositionRequest request) {
		positionService.addPosition(request);
	}

	@PatchMapping("/positions")
	@PreAuthorize("hasAuthority('system:position:update')")
	public void updatePosition(@RequestBody PositionRequest request) {
		positionService.updatePosition(request);
	}

	@DeleteMapping("/positions/{id}")
	@PreAuthorize("hasAuthority('system:position:delete')")
	public void deletePosition(@PathVariable String id) {
		positionService.deletePosition(id);
	}

	@GetMapping("/orgs")
	@PreAuthorize("hasAuthority('system:org:list')")
	public List<OrgView> listOrg() {
		return orgService.listOrg();
	}

	@PostMapping("/orgs")
	@PreAuthorize("hasAuthority('system:org:create')")
	public void addOrg(@RequestBody OrgRequest request) {
		orgService.addOrg(request);
	}

	@PatchMapping("/orgs")
	@PreAuthorize("hasAuthority('system:org:update')")
	public void updateOrg(@RequestBody OrgRequest request) {
		orgService.updateOrg(request);
	}

	@DeleteMapping("/orgs/{id}")
	@PreAuthorize("hasAuthority('system:org:delete')")
	public void deleteOrg(@PathVariable String id) {
		orgService.deleteOrg(id);
	}

	@PostMapping("/orgs/sort")
	@PreAuthorize("hasAuthority('system:org:create')")
	public void sortOrg(@RequestBody OrgSortRequest request) {
		orgService.sortOrg(request);
	}

}
