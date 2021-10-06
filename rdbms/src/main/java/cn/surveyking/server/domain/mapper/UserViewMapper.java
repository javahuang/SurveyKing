package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.uitls.SpringContextHolder;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.model.User;
import cn.surveyking.server.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Mapper(componentModel = "spring")
public interface UserViewMapper {

	@Mapping(target = "authorities", ignore = true)
	UserView toUserView(User user);

	List<UserView> toUserView(List<User> users);

	default UserView toUserViewById(String id) {
		if (id == null) {
			return null;
		}
		UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
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
