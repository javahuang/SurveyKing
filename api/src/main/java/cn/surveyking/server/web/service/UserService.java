package cn.surveyking.server.web.service;

import cn.surveyking.server.web.domain.dto.UserView;
import cn.surveyking.server.web.domain.dto.CreateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserService extends UserDetailsService {

	UserView create(CreateUserRequest request);

}
