package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.CreateUserRequest;
import cn.surveyking.server.domain.dto.UpdateUserRequest;
import cn.surveyking.server.domain.model.Account;
import cn.surveyking.server.domain.model.User;
import org.mapstruct.*;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Mapper
public interface UserEditMapper {

	User create(CreateUserRequest request);

	@Mapping(target = "authAccount", source = "username")
	Account createAccount(CreateUserRequest request);

	@BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
	// @Mapping(target = "authorities", ignore = true)
	void update(UpdateUserRequest request, @MappingTarget User user);

}
