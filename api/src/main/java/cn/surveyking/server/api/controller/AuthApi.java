package cn.surveyking.server.api.controller;

import cn.surveyking.server.api.domain.dto.AuthRequest;
import cn.surveyking.server.api.domain.dto.CreateUserRequest;
import cn.surveyking.server.api.domain.dto.UserView;
import cn.surveyking.server.api.domain.mapper.UserViewMapper;
import cn.surveyking.server.api.domain.model.User;
import cn.surveyking.server.api.service.UserService;
import cn.surveyking.server.core.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class AuthApi {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	private final UserViewMapper userViewMapper;

	private final UserService userService;

	@PostMapping("login")
	public ResponseEntity<UserView> login(@RequestBody @Valid AuthRequest request) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = (User) authenticate.getPrincipal();

		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
				.body(userViewMapper.toUserView(user));
	}

	@PostMapping("register")
	public UserView register(@RequestBody @Valid CreateUserRequest request) {
		return userService.create(request);
	}

}
