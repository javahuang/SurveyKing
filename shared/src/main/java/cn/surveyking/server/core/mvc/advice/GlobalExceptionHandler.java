package cn.surveyking.server.core.mvc.advice;

import cn.surveyking.server.core.common.ApiResponse;
import cn.surveyking.server.core.common.ResponseCode;
import cn.surveyking.server.core.exception.InternalServerError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Map;

/**
 * @author javahuang
 * @date 2021/08/13
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@Value("classpath:/static/index.html")
	private Resource indexHtml;

	@ExceptionHandler(NoHandlerFoundException.class)
	public Object handleError404(HttpServletRequest request, Exception e) {
		return ResponseEntity.ok().body(indexHtml);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiResponse<String>> handleValidationException(HttpServletRequest request,
			ValidationException ex) {
		log.error("ValidationException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.ok().body(new ApiResponse<>(ResponseCode.FAIL.code, "Validation exception"));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ApiResponse<String>> handleMissingServletRequestParameterException(HttpServletRequest request,
			MissingServletRequestParameterException ex) {
		log.error("handleMissingServletRequestParameterException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.ok().body(new ApiResponse<>(ResponseCode.FAIL.code, "Missing request parameter"));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentTypeMismatchException(
			HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
		log.error("handleMethodArgumentTypeMismatchException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.ok().body(new ApiResponse<>(ResponseCode.FAIL.code, "Method argument type mismatch"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
			HttpServletRequest request, MethodArgumentNotValidException ex) {
		log.error("handleMethodArgumentNotValidException {}\n", request.getRequestURI(), ex);
		return ResponseEntity.ok().body(new ApiResponse<>(ResponseCode.FAIL.code, "Method argument validation failed"));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(HttpServletRequest request,
			AccessDeniedException ex) {
		log.error("handleAccessDeniedException {}\n", request.getRequestURI());

		return ResponseEntity.ok().body(new ApiResponse<>(ResponseCode.FORBIDDEN.code, "Authentication failed"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<String>> handleInternalServerError(HttpServletRequest request, Exception ex) {
		log.error("handleInternalServerError {}\n", request.getRequestURI(), ex);
		return ResponseEntity.ok().body(new ApiResponse<>(ResponseCode.INTERNAL_SERVER_ERROR.code,
				ex instanceof InternalServerError ? ex.getMessage() : "Internal server error"));
	}

}
