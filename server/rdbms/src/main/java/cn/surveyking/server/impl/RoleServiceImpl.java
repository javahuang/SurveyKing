package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.domain.dto.SelectRoleRequest;
import cn.surveyking.server.domain.mapper.RoleViewMapper;
import cn.surveyking.server.domain.model.Role;
import cn.surveyking.server.mapper.RoleMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseService<RoleMapper, Role> implements RoleService {

	private final RoleViewMapper roleViewMapper;

	@Override
	public List<RoleView> selectRoles(SelectRoleRequest request) {
		return list().stream().map(role -> roleViewMapper.toView(role)).collect(Collectors.toList());
	}

}
