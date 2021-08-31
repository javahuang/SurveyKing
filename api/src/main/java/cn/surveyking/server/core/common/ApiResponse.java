package cn.surveyking.server.core.common;

import cn.surveyking.server.core.uitls.JSONUtil;
import lombok.Data;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
public class ApiResponse<T> {

	private int code;

	private String message;

	private T data;

	public ApiResponse() {
	}

	public ApiResponse(int code, T data) {
		this.code = code;
		this.data = data;
	}

	public ApiResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return JSONUtil.toJSONString(this);
	}

}
