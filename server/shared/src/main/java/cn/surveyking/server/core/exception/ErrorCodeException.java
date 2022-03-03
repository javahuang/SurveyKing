package cn.surveyking.server.core.exception;

import cn.surveyking.server.core.constant.ErrorCode;

/**
 * 定义异常码消息，方便后面前端进行国际化
 *
 * @author javahuang
 * @date 2022/2/23
 */
public class ErrorCodeException extends RuntimeException {

	private ErrorCode errorCode;

	public ErrorCodeException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
