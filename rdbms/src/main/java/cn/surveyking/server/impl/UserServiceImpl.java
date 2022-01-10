package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.security.PreAuthorizeAnnotationExtractor;
import cn.surveyking.server.domain.dto.UserInfo;
import cn.surveyking.server.domain.dto.UserQuery;
import cn.surveyking.server.domain.dto.UserRequest;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.model.*;
import cn.surveyking.server.domain.mapper.RoleViewMapper;
import cn.surveyking.server.domain.mapper.UserPositionDtoMapper;
import cn.surveyking.server.domain.mapper.UserViewMapper;
import cn.surveyking.server.mapper.*;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
@Slf4j
public class UserServiceImpl extends BaseService<UserMapper, User> implements UserService {

	private final AccountMapper accountMapper;

	private final PasswordEncoder passwordEncoder;

	private final RoleViewMapper roleViewMapper;

	private final UserViewMapper userViewMapper;

	private final UserRoleMapper userRoleMapper;

	private final RoleServiceImpl roleService;

	private final UserPositionMapper userPositionMapper;

	private final UserPositionDtoMapper userPositionDtoMapper;

	private final OrgMapper orgMapper;

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
			throw new UsernameNotFoundException(format("用户: {}, 不存在", username));
		}
		if (existAccount.getStatus() != AppConsts.USER_STATUS.VALID.getStatus()) {
			throw new AccessDeniedException("用户: {}, 被禁用");
		}

		return userViewMapper.toUserView(existAccount);
	}

	@Override
	@Cacheable(cacheNames = "userCache", key = "#p0", condition = "#p0!=null", unless = "#result == null")
	public UserInfo loadUserById(String userId) {
		User user = this.getById(userId);
		if (user == null) {
			return null;
		}
		UserInfo userInfo = userViewMapper.toUserInfo(user);
		List<Role> roles = userRoleMapper
				.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, user.getId())).stream()
				.map(ur -> roleService.getById(ur.getRoleId())).collect(Collectors.toList());
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
		List<String> orgIds = getChildOrgIds(query.getOrgId());
		Page<User> userPage = pageByQuery(query,
				Wrappers.<User>lambdaQuery().like(isNotBlank(query.getName()), User::getName, query.getName())
						.in(orgIds.size() > 0, User::getOrgId, orgIds).in(query.getIds() != null, User::getId,
								Arrays.asList(query.getIds() != null ? query.getIds() : new String[0])));
		return new PaginationResponse<>(userPage.getTotal(), userPage.getRecords().stream().map(x -> {
			UserView userView = userViewMapper.toUserView(x);
			userView.setUsername(accountMapper
					.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, x.getId())).getAuthAccount());
			// 设置用户部门
			Org org = orgMapper.selectById(x.getOrgId());
			if (org != null) {
				userView.setOrgName(org.getName());
			}
			// 设置用户角色
			userView.setRoles(
					userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, x.getId()))
							.stream().map(userRole -> roleViewMapper.toView(roleService.getById(userRole.getRoleId())))
							.collect(Collectors.toList()));
			// 设置用户岗位
			userView.setUserPositions(userPositionDtoMapper.toView(userPositionMapper
					.selectList(Wrappers.<UserPosition>lambdaQuery().eq(UserPosition::getUserId, x.getId()))));
			return userView;
		}).collect(Collectors.toList()));
	}

	private List<String> getChildOrgIds(String parentOrgId) {
		List<String> result = new ArrayList<>();
		if (parentOrgId == null) {
			return result;
		}
		result.add(parentOrgId);
		orgMapper.selectList(Wrappers.<Org>lambdaQuery().eq(Org::getParentId, parentOrgId)).forEach(org -> {
			result.addAll(getChildOrgIds(org.getId()));
		});
		return result;
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
		request.setId(user.getId());
		addUserRoles(request);
		// 添加用户岗位
		addUserPositions(request);
	}

	private void addUserRoles(UserRequest request) {
		request.getRoles().forEach(roleId -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(request.getId());
			userRole.setRoleId(roleId);
			userRole.setUserType(AppConsts.USER_TYPE.SysUser.name());
			userRoleMapper.insert(userRole);
		});
	}

	private void addUserPositions(UserRequest request) {
		if (request.getUserPositions() != null) {
			request.getUserPositions().forEach(userPositionRequest -> {
				UserPosition userPosition = userPositionDtoMapper.fromRequest(userPositionRequest);
				userPosition.setUserId(request.getId());
				userPositionMapper.insert(userPosition);
			});
		}
	}

	@Override
	public void updateUser(UserRequest request) {
		if (request.getId() == null) {
			return;
		}
		User user = userViewMapper.toUser(request);
		this.updateById(user);

		// 更新登录账号
		Account account = userViewMapper.toAccount(request);
		if (isNotBlank(request.getPassword())) {
			account.setAuthSecret(passwordEncoder.encode(request.getPassword()));
		}
		accountMapper.update(account, Wrappers.<Account>lambdaQuery().eq(Account::getUserId, request.getId()));

		// 更新用户角色
		userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, request.getId()));
		addUserRoles(request);
		// 更新用户岗位
		userPositionMapper.delete(Wrappers.<UserPosition>lambdaQuery().eq(UserPosition::getUserId, request.getId()));
		addUserPositions(request);
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

	@Override
	public void updateUserPosition(UserRequest request) {
		userPositionMapper
				.delete(Wrappers.<UserPosition>lambdaQuery().eq(UserPosition::getPositionId, request.getId()));
		request.getUserPositions().forEach(userPositionRequest -> {
			UserPosition position = userPositionDtoMapper.fromRequest(userPositionRequest);
			position.setUserId(request.getId());
			userPositionMapper.insert(position);
		});
	}

	@Override
	public Set<String> getUserGroups(String userId) {
		Set<String> groups = new LinkedHashSet<>();
		// 获取用户的系统角色
		userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId)).forEach(role -> {
			groups.add("R:" + role.getRoleId());
		});
		// 获取用户的用户岗位
		userPositionMapper.selectList(Wrappers.<UserPosition>lambdaQuery().eq(UserPosition::getUserId, userId))
				.forEach(userPosition -> {
					groups.add("P:" + userPosition.getOrgId() + ":" + userPosition.getPositionId());
					groups.add("P:" + "ALL:" + userPosition.getPositionId());
					groups.add("P:" + userPosition.getOrgId() + ":ALL");
				});
		User current = getById(userId);
		// groups.add("U:" + current.getId());
		// groups.add("P:" + current.getOrgId() + ":");
		return groups;
	}

	@Override
	public void init() {
		if (count() > 0) {
			return;
		}
		log.info("开始初始化系统用户");
		// 创建角色
		Role role = new Role();
		role.setName("Admin");
		role.setCode("admin");
		role.setRemark("系统初始化角色");
		role.setAuthority(String.join(",", PreAuthorizeAnnotationExtractor.extractAllApiPermissions()));
		roleService.save(role);

		// 创建用户
		User user = new User();
		user.setName("Admin");
		user.setGender("M");
		user.setStatus(AppConsts.USER_STATUS.VALID.getStatus());
		save(user);

		// 绑定用户角色
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		userRole.setRoleId(role.getId());
		userRole.setUserType(AppConsts.USER_TYPE.SysUser.name());
		userRoleMapper.insert(userRole);

		// 创建账号
		Account account = new Account();
		account.setAuthAccount("admin");
		account.setAuthSecret(passwordEncoder.encode("surveyking"));
		account.setUserId(user.getId());
		account.setUserType(AppConsts.USER_TYPE.SysUser.name());
		account.setStatus(AppConsts.USER_STATUS.VALID.getStatus());
		accountMapper.insert(account);
		log.info("系统用户初始化完成(admin/surveyking)");
	}

}
