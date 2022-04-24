package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2022/2/23
 */
public enum ErrorCode {

	/** 账号或者密码错误 */
	UsernameOrPasswordError(1024, "账号或者密码错误"),
	/** 账号已存在 */
	UsernameExists(1025, "账号已存在"),
	/** 暂停回收 */
	SurveySuspend(4000, "问卷已暂停回收"),
	/** 页面不存在 */
	ProjectNotFound(4004, "对不起，你访问的页面不存在"),
	/** 密码验证失败 */
	ValidationError(4005, "密码验证失败"),
	/** 停止收集 */
	ExceededMaxAnswers(4010, "已达到回收上限，问卷停止收集"),
	/** 停止收集 */
	ExceededEndTime(4011, "超出截止时间，问卷停止收集"),
	/** 问卷已提交 */
	SurveySubmitted(4012, "问卷已提交"),
	/** 答案不允许修改 */
	AnswerChangeDisabled(4020, "答案不允许修改"),
	/** 附件不存在 */
	FileNotExists(4040, "附件不存在"),
	/** 文件上传失败 */
	FileUploadError(4041, "文件上传失败");

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
