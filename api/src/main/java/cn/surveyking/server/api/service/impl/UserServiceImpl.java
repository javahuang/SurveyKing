package cn.surveyking.server.api.service.impl;

import cn.surveyking.server.api.domain.dto.UserView;
import cn.surveyking.server.api.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.surveyking.server.api.domain.dto.CreateUserRequest;
import cn.surveyking.server.api.domain.mapper.UserEditMapper;
import cn.surveyking.server.api.domain.mapper.UserViewMapper;
import cn.surveyking.server.api.domain.model.User;
import cn.surveyking.server.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.HashSet;

import static java.lang.String.format;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	private final UserEditMapper userEditMapper;

	private final UserViewMapper userViewMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = loadUser(username);
		if (user == null) {
			throw new UsernameNotFoundException(format("User: %s, not found", username));
		}
		return user;
	}

	private User loadUser(String username) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("username", username);
		// TODO: 此处添加缓存
		return userMapper.selectOne(wrapper);
	}

	@Override
	public UserView create(CreateUserRequest request) {
		if (loadUser(request.getUsername()) != null) {
			throw new ValidationException("用户名已存在!");
		}
		if (!request.getPassword().equals(request.getRePassword())) {
			throw new ValidationException("两次输入密码不一致!");
		}
		if (request.getAuthorities() == null) {
			request.setAuthorities(new HashSet<>());
		}
		User user = userEditMapper.create(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		userMapper.insert(user);

		return userViewMapper.toUserView(user);
	}

}
