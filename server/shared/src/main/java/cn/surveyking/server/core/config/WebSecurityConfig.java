package cn.surveyking.server.core.config;

import cn.surveyking.server.core.security.JwtTokenFilter;
import cn.surveyking.server.core.security.RestAuthenticationEntryPoint;
import cn.surveyking.server.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtTokenFilter jwtTokenFilter;

	private final UserService userService;

	private final RestAuthenticationEntryPoint authenticationEntryPoint;

	public WebSecurityConfig(JwtTokenFilter jwtTokenFilter, UserService userService,
			RestAuthenticationEntryPoint authenticationEntryPoint) {
		this.jwtTokenFilter = jwtTokenFilter;
		this.userService = userService;
		this.authenticationEntryPoint = authenticationEntryPoint;
		// 允许在 @Async 方法里面获取 SecurityContext
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> userService.loadUserByUsername(username));
		// 添加更多类型的认证方式 auth.authenticationProvider();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 设置允许 iframe 引用
		http.headers().frameOptions().disable();
		http = http.cors().and().csrf().disable();
		http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

		http = http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and();
		// 所有请求都放行，目的是单 jar 部署，输入任意路由也能跳转到对应的页面，权限拦截通过注解配置
		http.authorizeRequests().antMatchers("/api/public/**").permitAll().antMatchers("/api/system").permitAll()
				.antMatchers(HttpMethod.GET, "/api/files").permitAll().antMatchers("/api/**").authenticated()
				.antMatchers("/").permitAll();

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	// Expose authentication manager bean
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	// Remove the default ROLE_ prefix
	// @Bean
	// public GrantedAuthorityDefaults grantedAuthorityDefaults() {
	// return new GrantedAuthorityDefaults("");
	// }

}
