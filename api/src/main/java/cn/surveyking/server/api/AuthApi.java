package cn.surveyking.server.api;

import cn.surveyking.server.core.security.JwtTokenUtil;
import cn.surveyking.server.domain.dto.AuthRequest;
import cn.surveyking.server.domain.dto.CreateUserRequest;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.service.UserService;
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

	private final UserService userService;

	@PostMapping("login")
	public ResponseEntity<UserView> login(@RequestBody @Valid AuthRequest request) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		UserView user = (UserView) authenticate.getPrincipal();

		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user)).body(user);
	}

	@PostMapping("register")
	public UserView register(@RequestBody @Valid CreateUserRequest request) {
		return userService.create(request);
	}

}
