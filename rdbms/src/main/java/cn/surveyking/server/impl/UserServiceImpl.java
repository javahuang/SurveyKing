package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.security.PreAuthorizeAnnotationExtractor;
import cn.surveyking.server.domain.dto.CreateUserRequest;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.mapper.UserEditMapper;
import cn.surveyking.server.domain.mapper.UserViewMapper;
import cn.surveyking.server.domain.model.Account;
import cn.surveyking.server.domain.model.Role;
import cn.surveyking.server.domain.model.User;
import cn.surveyking.server.domain.model.UserRole;
import cn.surveyking.server.mapper.AccountMapper;
import cn.surveyking.server.mapper.RoleMapper;
import cn.surveyking.server.mapper.UserMapper;
import cn.surveyking.server.mapper.UserRoleMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService<UserMapper, User> implements UserService {

	private final AccountMapper accountMapper;

	private final PasswordEncoder passwordEncoder;

	private final UserEditMapper userEditMapper;

	private final UserViewMapper userViewMapper;

	private final UserRoleMapper userRoleMapper;

	private final RoleMapper roleMapper;

	/**
	 * @param username 账号密码登录认证使用
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserView loadUserByUsername(String username) throws UsernameNotFoundException {
		LambdaQueryWrapper<Account> queryWrapper = Wrappers.<Account>lambdaQuery().eq(Account::getAuthAccount,
				username);
		Account existAccount = accountMapper.selectOne(queryWrapper);
		if (existAccount == null) {
			throw new UsernameNotFoundException(format("用户: %s, 不存在", username));
		}
		if (existAccount.getStatus() != AppConsts.USER_STATUS.VALID.getStatus()) {
			throw new AccessDeniedException("用户: %s, 被禁用");
		}

		return userViewMapper.toUserView(existAccount);
	}

	@Override
	@Cacheable(cacheNames = "userCache", key = "#userId", unless = "#result == null")
	public UserView currentUser(String userId) {
		User user = this.getById(userId);
		UserView view = userViewMapper.toUserView(user);
		List<Role> roles = userRoleMapper
				.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, user.getId())).stream()
				.map(ur -> roleMapper.selectById(ur.getRoleId())).collect(Collectors.toList());
		Set<String> authorities = new HashSet<>();
		roles.forEach(role -> {
			authorities.add("ROLE_" + role.getCode());
			Arrays.stream(role.getAuthority().split(",")).forEach(authority -> {
				authorities.add(authority);
			});
		});
		view.setAuthorities(
				authorities.stream().map(authority -> (GrantedAuthority) () -> authority).collect(Collectors.toSet()));
		return view;
	}

	@Override
	public void create(CreateUserRequest request) {
		LambdaQueryWrapper<Account> queryWrapper = Wrappers.<Account>lambdaQuery().eq(Account::getAuthAccount,
				request.getUsername());
		Account existAccount = accountMapper.selectOne(queryWrapper);
		if (existAccount != null) {
			throw new ValidationException("用户名已存在!");
		}
		if (!request.getPassword().equals(request.getRePassword())) {
			throw new ValidationException("两次输入密码不一致!");
		}
		if (request.getAuthorities() == null) {
			request.setAuthorities(new HashSet<>());
		}
		User user = userEditMapper.create(request);
		this.save(user);

		// 创建登录账号
		Account account = userEditMapper.createAccount(request);
		account.setAuthType(AppConsts.AUTH_TYPE.PWD.name());
		account.setUserType(AppConsts.USER_TYPE.SysUser.toString());
		account.setAuthSecret(passwordEncoder.encode(request.getPassword()));
		account.setUserId(user.getId());
		accountMapper.insert(account);

		// 添加角色
		Role role = new Role();
		String authority = PreAuthorizeAnnotationExtractor.extractAllApiPermissions().stream()
				.collect(Collectors.joining(","));
		role.setAuthority(authority);
		role.setName("管理员");
		role.setCode("admin");
		roleMapper.insert(role);

		// 添加用户角色
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		userRole.setRoleId(role.getId());
		userRole.setUserType(AppConsts.USER_TYPE.SysUser.name());
		userRoleMapper.insert(userRole);
	}

}
