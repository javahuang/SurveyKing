package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.service.SystemService;
import cn.surveyking.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping("/roles")
	@PreAuthorize("hasAuthority('system:role')")
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
	@PreAuthorize("hasAuthority('system:user')")
	public PaginationResponse<UserView> roles(UserQuery query) {
		return userService.getUsers(query);
	}

	@PostMapping("/users")
	@PreAuthorize("hasAuthority('system:user:create')")
	public void createRole(@RequestBody UserRequest request) {
		userService.createUser(request);
	}

	@PatchMapping("/users/{id}")
	@PreAuthorize("hasAuthority('system:user:update')")
	public void updateRole(@PathVariable("id") String id, @RequestBody UserRequest request) {
		request.setId(id);
		userService.updateUser(request);
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
}
