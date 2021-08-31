package cn.surveyking.server.core.security;

import cn.surveyking.server.core.common.ApiResponse;
import cn.surveyking.server.core.common.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 捕获 spring security 认证失败异常，返回 application/json 类型的数据
 *
 * @author javahuang
 * @date 2021/8/24
 */
@Component("restAuthenticationEntryPoint")
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {

		response.setContentType("application/json");
		ApiResponse<String> apiResponse = new ApiResponse(ResponseCode.UNAUTHORIZED.code,
				authenticationException.getMessage());

		response.setStatus(HttpServletResponse.SC_OK);
		response.getOutputStream().println(objectMapper.writeValueAsString(apiResponse));

	}

}