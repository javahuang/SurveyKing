package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.CreateUserRequest;
import cn.surveyking.server.domain.dto.UpdateUserRequest;
import cn.surveyking.server.domain.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Mapper(componentModel = "spring")
public interface UserEditMapper {

	User create(CreateUserRequest request);

	@BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
	// @Mapping(target = "authorities", ignore = true)
	void update(UpdateUserRequest request, @MappingTarget User user);

}
