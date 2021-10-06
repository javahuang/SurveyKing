package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.CreateUserRequest;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.mapper.UserEditMapper;
import cn.surveyking.server.domain.mapper.UserViewMapper;
import cn.surveyking.server.domain.model.User;
import cn.surveyking.server.mapper.UserMapper;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
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
	public UserView loadUserByUsername(String username) throws UsernameNotFoundException {
		UserView user = loadUser(username);
		if (user == null) {
			throw new UsernameNotFoundException(format("User: %s, not found", username));
		}
		return user;
	}

	private UserView loadUser(String username) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("username", username);
		// TODO: 此处添加缓存
		User user = userMapper.selectOne(wrapper);
		return userViewMapper.toUserView(user);
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
