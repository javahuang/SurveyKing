package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.CacheConsts;
import cn.surveyking.server.domain.dto.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/12
 */
public interface SystemService {

	@Cacheable(value = CacheConsts.commonCacheName, key = "'systemInfo'")
	SystemInfo getSystemInfo();

	@Caching(evict = {
			@CacheEvict(value = CacheConsts.commonCacheName, key = "'systemInfo'"),
			@CacheEvict(value = CacheConsts.commonCacheName, key = "'aiInfo'")
	})
	void updateSystemInfo(SystemInfoRequest request);

	PaginationResponse<RoleView> getRoles(RoleQuery query);

	void createRole(RoleRequest request);

	void updateRole(RoleRequest request);

	void deleteRole(RoleRequest request);

	List<PermissionView> getPermissions();

	void extractCodeDiffDbPermissions();

	@Cacheable(value = CacheConsts.commonCacheName, key = "'aiInfo'")
	SystemInfo.AiSetting getSystemAiSetting();
}
