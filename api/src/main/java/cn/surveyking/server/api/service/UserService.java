package cn.surveyking.server.api.service;

import cn.surveyking.server.api.domain.dto.UserView;
import cn.surveyking.server.api.domain.dto.CreateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserService extends UserDetailsService {

	UserView create(CreateUserRequest request);

}
