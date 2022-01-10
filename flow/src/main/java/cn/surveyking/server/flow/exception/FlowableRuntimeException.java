package cn.surveyking.server.flow.exception;

/**
 * @author javahuang
 * @date 2021/11/18
 */
public class FlowableRuntimeException extends RuntimeException {

	public FlowableRuntimeException(String message) {
		super(message);
	}

	public FlowableRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
