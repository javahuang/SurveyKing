package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.RoleRequest;
import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.domain.model.Role;
import org.mapstruct.*;

import java.util.Arrays;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Mapper
public interface RoleViewMapper {

	RoleView toView(Role role);

	Role fromRequest(RoleRequest roleRequest);

	@AfterMapping
	default void afterMapping(Role role, @MappingTarget RoleView target) {
		target.setAuthorities(Arrays.asList(role.getAuthority().split(",")));
	}

	@AfterMapping
	default void afterMappingRole(RoleRequest request, @MappingTarget Role target) {
		target.setAuthority(String.join(",", request.getAuthorities()));
	}

}
