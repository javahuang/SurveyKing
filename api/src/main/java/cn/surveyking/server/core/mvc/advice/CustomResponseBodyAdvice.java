package cn.surveyking.server.core.mvc.advice;

import cn.surveyking.server.core.common.ApiResponse;
import cn.surveyking.server.core.common.ResponseCode;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author javahuang
 * @date 2021/08/23
 */
@RestControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body instanceof Resource) {
			return body;
		}
		if (body instanceof ApiResponse) {
			return body;
		}
		return new ApiResponse(ResponseCode.SUCCESS.code, body);
	}

}
