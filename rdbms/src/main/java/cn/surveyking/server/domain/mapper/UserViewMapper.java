package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.domain.dto.UserInfo;
import cn.surveyking.server.domain.dto.UserRequest;
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
	UserInfo toUserInfo(User user);

	UserView toUserView(User user);

	List<UserView> toUserView(List<User> users);

	@Mapping(source = "authAccount", target = "username")
	@Mapping(source = "authSecret", target = "password")
	UserInfo toUserView(Account account);

	default UserInfo toUserViewById(String id) {
		if (id == null) {
			return null;
		}
		UserMapper userMapper = ContextHelper.getBean(UserMapper.class);
		return toUserInfo(userMapper.selectById(id));
	}

	User toUser(UserRequest request);

	@Mapping(target = "authAccount", source = "username")
	@Mapping(target = "userId", source = "id")
	Account toAccount(UserRequest request);

}
