package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.domain.dto.RoleRequest;
import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.domain.model.Role;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Mapper
public interface RoleViewMapper extends BaseModelMapper<RoleRequest, RoleView, Role> {

	@AfterMapping
	default void afterMapping(Role role, @MappingTarget RoleView target) {
		if(role.getAuthority() != null) {
			target.setAuthorities(Arrays.asList(role.getAuthority().split(",")));
		}

	}

	@AfterMapping
	default void afterMappingRole(RoleRequest request, @MappingTarget Role target) {
		if (CollectionUtils.isEmpty(request.getAuthorities())) {
			return;
		}
		target.setAuthority(String.join(",", request.getAuthorities()));
	}

}
