package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.CacheConsts;
import cn.surveyking.server.core.security.PreAuthorizeAnnotationExtractor;
import cn.surveyking.server.domain.dto.PermissionView;
import cn.surveyking.server.domain.dto.RoleQuery;
import cn.surveyking.server.domain.dto.RoleRequest;
import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.domain.mapper.RoleViewMapper;
import cn.surveyking.server.domain.model.Role;
import cn.surveyking.server.domain.model.UserRole;
import cn.surveyking.server.mapper.UserRoleMapper;
import cn.surveyking.server.service.SystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemServiceImpl implements SystemService {

	private final RoleServiceImpl roleService;

	private final RoleViewMapper roleViewMapper;

	private final CacheManager cacheManager;

	private final UserRoleMapper userRoleMapper;

	@Override
	public PaginationResponse<RoleView> getRoles(RoleQuery query) {
		Page<Role> rolePage = roleService.pageByQuery(query,
				Wrappers.<Role>lambdaQuery().like(isNotBlank(query.getName()), Role::getName, query.getName()));
		return new PaginationResponse<>(rolePage.getTotal(),
				rolePage.getRecords().stream().map(x -> roleViewMapper.toView(x)).collect(Collectors.toList()));
	}

	@Override
	public void createRole(RoleRequest request) {
		roleService.save(roleViewMapper.fromRequest(request));
	}

	@Override
	public void updateRole(RoleRequest request) {
		roleService.updateById(roleViewMapper.fromRequest(request));
		evictCache(request.getId());
	}

	@Override
	public void deleteRole(String id) {
		roleService.removeById(id);
		evictCache(id);
	}

	/**
	 * 角色信息变化时，清除对应的 cache 缓存
	 * @param roleId
	 */
	private void evictCache(String roleId) {
		userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, roleId)).forEach(
				userRole -> cacheManager.getCache(CacheConsts.userCacheName).evictIfPresent(userRole.getUserId()));
	}

	@Override
	public List<PermissionView> getPermissions() {
		return PreAuthorizeAnnotationExtractor.extractAllApiPermissions().stream().map(x -> new PermissionView(x))
				.collect(Collectors.toList());
	}

	@Override
	public void extractCodeDiffDbPermissions() {

	}

}
