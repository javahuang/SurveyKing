package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.CreateUserRequest;
import cn.surveyking.server.domain.dto.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserService extends UserDetailsService {

	UserView create(CreateUserRequest request);

}
