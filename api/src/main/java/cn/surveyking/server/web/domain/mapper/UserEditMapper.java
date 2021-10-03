package cn.surveyking.server.web.domain.mapper;

import cn.surveyking.server.web.domain.dto.UpdateUserRequest;
import cn.surveyking.server.web.domain.model.Role;
import cn.surveyking.server.web.domain.dto.CreateUserRequest;
import cn.surveyking.server.web.domain.model.User;
import org.mapstruct.*;

import static java.util.stream.Collectors.toSet;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Mapper(componentModel = "spring")
public interface UserEditMapper {

	@Mapping(target = "authorities", ignore = true)
	User create(CreateUserRequest request);

	@BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
	@Mapping(target = "authorities", ignore = true)
	void update(UpdateUserRequest request, @MappingTarget User user);

	@AfterMapping
	default void afterCreate(CreateUserRequest request, @MappingTarget User user) {
		if (request.getAuthorities() != null) {
			user.setAuthorities(request.getAuthorities().stream().map(Role::new).collect(toSet()));
		}
	}

	@AfterMapping
	default void afterUpdate(UpdateUserRequest request, @MappingTarget User user) {
		if (request.getAuthorities() != null) {
			user.setAuthorities(request.getAuthorities().stream().map(Role::new).collect(toSet()));
		}
	}

}
