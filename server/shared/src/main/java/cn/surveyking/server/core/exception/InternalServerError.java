package cn.surveyking.server.core.exception;

/**
 * @author javahuang
 * @date 2021/8/6
 */
public class InternalServerError extends RuntimeException {

	public InternalServerError() {
		super();
	}

	public InternalServerError(String message) {
		super(message);
	}

	public InternalServerError(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalServerError(Throwable cause) {
		super(cause);
	}

}
