package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.domain.dto.UserInfo;
import cn.surveyking.server.domain.dto.UserQuery;
import cn.surveyking.server.domain.dto.UserRequest;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.mapper.RoleViewMapper;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	private final RoleViewMapper roleViewMapper;

	private final UserViewMapper userViewMapper;

	private final UserRoleMapper userRoleMapper;

	private final RoleMapper roleMapper;

	/**
	 * @param username 账号密码登录认证使用
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
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
	public UserInfo currentUser(String userId) {
		User user = this.getById(userId);
		UserInfo userInfo = userViewMapper.toUserInfo(user);
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
		userInfo.setAuthorities(
				authorities.stream().map(authority -> (GrantedAuthority) () -> authority).collect(Collectors.toSet()));
		return userInfo;
	}

	@Override
	public PaginationResponse<UserView> getUsers(UserQuery query) {
		Page<User> userPage = pageByQuery(query,
				Wrappers.<User>lambdaQuery().like(isNotBlank(query.getName()), User::getName, query.getName()));
		return new PaginationResponse<>(userPage.getTotal(), userPage.getRecords().stream().map(x -> {
			UserView userView = userViewMapper.toUserView(x);
			userView.setUsername(accountMapper
					.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, x.getId())).getAuthAccount());
			userView.setRoles(userRoleMapper
					.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, x.getId())).stream()
					.map(userRole -> roleViewMapper.toView(roleMapper.selectById(userRole.getRoleId())))
					.collect(Collectors.toList()));
			return userView;
		}).collect(Collectors.toList()));
	}

	@Override
	public void createUser(UserRequest request) {
		User user = userViewMapper.toUser(request);
		this.save(user);

		// 创建登录账号
		Account account = userViewMapper.toAccount(request);
		account.setAuthType(AppConsts.AUTH_TYPE.PWD.name());
		account.setUserType(AppConsts.USER_TYPE.SysUser.toString());
		account.setAuthSecret(passwordEncoder.encode(request.getPassword()));
		account.setUserId(user.getId());
		accountMapper.insert(account);

		// 添加用户角色
		request.getRoles().forEach(roleId -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			userRole.setUserType(AppConsts.USER_TYPE.SysUser.name());
			userRoleMapper.insert(userRole);
		});
	}

	@Override
	public void updateUser(UserRequest request) {
		if (request.getId() == null) {
			return;
		}
		User user = userViewMapper.toUser(request);
		this.updateById(user);

		// 创建登录账号
		Account account = userViewMapper.toAccount(request);
		accountMapper.update(account, Wrappers.<Account>lambdaQuery().eq(Account::getUserId, request.getId()));

		// 添加用户角色
		userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, request.getId()));
		request.getRoles().forEach(roleId -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			userRole.setUserType(AppConsts.USER_TYPE.SysUser.name());
			userRoleMapper.insert(userRole);
		});
	}

	@Override
	public void deleteUser(String id) {
		removeById(id);
		accountMapper.delete(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, id));
	}

	@Override
	public boolean checkUsernameExist(String username) {
		Account account = accountMapper
				.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getAuthAccount, username));
		if (account != null) {
			return true;
		}
		return false;
	}

}
