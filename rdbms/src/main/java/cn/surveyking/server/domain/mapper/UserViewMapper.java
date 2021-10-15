package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.model.Account;
import cn.surveyking.server.domain.model.User;
import cn.surveyking.server.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Mapper
public interface UserViewMapper {

	@Mapping(target = "authorities", ignore = true)
	@Mapping(target = "userId", source = "id")
	UserView toUserView(User user);

	List<UserView> toUserView(List<User> users);

	@Mapping(source = "authAccount", target = "username")
	@Mapping(source = "authSecret", target = "password")
	UserView toUserView(Account account);

	default UserView toUserViewById(String id) {
		if (id == null) {
			return null;
		}
		UserMapper userMapper = ContextHelper.getBean(UserMapper.class);
		return toUserView(userMapper.selectById(id));
	}

	// @AfterMapping
	// default void afterCreate(CreateUserRequest request, @MappingTarget UserView user) {
	// if (request.getAuthorities() != null) {
	// user.setAuthorities(request.getAuthorities().stream().map(Role::new).collect(toSet()));
	// }
	// }
	//
	// @AfterMapping
	// default void afterUpdate(UpdateUserRequest request, @MappingTarget UserView user) {
	// if (request.getAuthorities() != null) {
	// user.setAuthorities(request.getAuthorities().stream().map(Role::new).collect(toSet()));
	// }
	// }

}
