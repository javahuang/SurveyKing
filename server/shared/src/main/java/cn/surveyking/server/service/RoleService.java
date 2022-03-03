package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.domain.dto.SelectRoleRequest;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/3
 */
public interface RoleService {

	List<RoleView> selectRoles(SelectRoleRequest request);

}
