package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2022/2/23
 */
public enum ErrorCode {

	ExceededMaxAnswers(4001, "问卷已停止收集"), ExceededEndTime(4002, "问卷已停止收集"), ProjectNotFound(4004, "对不起，你访问的页面不存在");

	/**
	 * 前两位区分模块，后两位区分错误消息
	 */
	public int code;

	public String message;

	ErrorCode(int code) {
		this.code = code;
	}

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

}
