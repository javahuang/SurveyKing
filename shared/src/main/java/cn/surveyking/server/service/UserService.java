package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserService extends UserDetailsService {

	UserInfo currentUser(String userId);

	PaginationResponse<UserView> getUsers(UserQuery query);

	void createUser(UserRequest request);

	void updateUser(UserRequest request);

	void deleteUser(String id);

	boolean checkUsernameExist(String username);

}
