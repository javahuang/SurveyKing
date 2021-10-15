package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/12
 */
public interface SystemService {

	PaginationResponse<RoleView> getRoles(RoleQuery query);

	void createRole(RoleRequest request);

	void updateRole(RoleRequest request);

	void deleteRole(String id);

	List<PermissionView> getPermissions();

	void extractCodeDiffDbPermissions();

}
