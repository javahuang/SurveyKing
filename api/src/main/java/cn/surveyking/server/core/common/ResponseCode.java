package cn.surveyking.server.core.common;

/**
 * @author javahuang
 * @date 2021/8/6
 */
public enum ResponseCode {

	SUCCESS(200), FAIL(400), UNAUTHORIZED(401), NOT_FOUND(404), INTERNAL_SERVER_ERROR(500);

	public int code;

	ResponseCode(int code) {
		this.code = code;
	}

}
