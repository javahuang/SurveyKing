package cn.surveyking.server.core.security;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;

/**
 * @author javahuang
 * @date 2021/8/23
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;

	private final UserService userService;

	private final RestAuthenticationEntryPoint resolveException;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// Get authorization cookie and validate
		Cookie tokenCookie = WebUtils.getCookie(request, AppConsts.COOKIE_TOKEN_NAME);
		if (tokenCookie == null) {
			chain.doFilter(request, response);
			return;
		}

		// Get jwt token and validate
		final String token = tokenCookie.getValue().trim();
		if (!jwtTokenUtil.validate(token)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			// Get user identity and set it on the spring security context
			UserDetails userDetails = userService.loadUserById(jwtTokenUtil.getUser(token).getUserId());

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, ofNullable(userDetails).map(UserDetails::getAuthorities).orElse(new ArrayList<>()));
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}
		catch (AuthenticationException e) {
			// spring security filter 里面的异常，GlobalExceptionHandler 不能捕获
			resolveException.commence(request, response, e);
		}

	}

}
