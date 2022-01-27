package cn.surveyking.server.api;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.security.JwtTokenUtil;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.AuthRequest;
import cn.surveyking.server.domain.dto.UserInfo;
import cn.surveyking.server.domain.dto.UserTokenView;
import cn.surveyking.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}")
public class UserApi {

	private final UserService userService;

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	@PostMapping("/public/login")
	public ResponseEntity login(@RequestBody @Valid AuthRequest request) {
		Authentication authentication;
		if (request.getAuthType() == AppConsts.AUTH_TYPE.PWD) {
			authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
		}
		else {
			throw new AccessDeniedException("不支持的认证方式");
		}
		// 将 token 提交给 spring security 的 DaoAuthenticationProvider 进行认证
		Authentication authenticate = authenticationManager.authenticate(authentication);
		UserInfo user = (UserInfo) authenticate.getPrincipal();
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,
				jwtTokenUtil.generateAccessToken(new UserTokenView(user.getUserId()))).build();
	}

	@GetMapping("/currentUser")
	@PreAuthorize("isAuthenticated()")
	public UserInfo currentUser() {
		return userService.loadUserById(SecurityContextUtils.getUserId());
	}

}
