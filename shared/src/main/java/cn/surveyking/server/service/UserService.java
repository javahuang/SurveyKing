package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.UserInfo;
import cn.surveyking.server.domain.dto.UserQuery;
import cn.surveyking.server.domain.dto.UserRequest;
import cn.surveyking.server.domain.dto.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserService extends UserDetailsService {

	UserInfo loadUserById(String userId);

	PaginationResponse<UserView> getUsers(UserQuery query);

	void createUser(UserRequest request);

	void updateUser(UserRequest request);

	void deleteUser(String id);

	boolean checkUsernameExist(String username);

	void updateUserPosition(UserRequest request);

	Set<String> getUserGroups(String userId);

	/**
	 * 系统用户初始化
	 */
	void init();

}
