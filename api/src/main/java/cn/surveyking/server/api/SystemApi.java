package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.PermissionView;
import cn.surveyking.server.domain.dto.RoleQuery;
import cn.surveyking.server.domain.dto.RoleRequest;
import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author javahuang
 * @date 2021/10/12
 */
@RestController
@RequestMapping("${api.prefix}/system")
@RequiredArgsConstructor
public class SystemApi {

	private final SystemService systemService;

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

}
