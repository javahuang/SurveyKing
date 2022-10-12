package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2022/2/23
 */
public enum ErrorCode {

	/** 账号或者密码错误 */
	UsernameOrPasswordError(1024, "账号或者密码错误"),
	/** 注册失败 */
	RegisterError(1401, "注册失败"),
	/** 账号已存在 */
	UsernameExists(1025, "账号已存在"),
	/** 暂停回收 */
	SurveySuspend(4000, "问卷已暂停回收"),
	/** 页面不存在 */
	ProjectNotFound(4004, "对不起，你访问的页面不存在"),
	/** 验证失败 */
	ValidationError(4005, "验证失败"),
	/** 权限校验失败 */
	PermVerifyFailed(4006, "没有权限访问本问卷"),
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
	FileUploadError(4041, "文件上传失败"),
	/** 文件解析失败 */
	FileParseError(4042, "文件解析失败"),
	/** 考试未开始 */
	ExamUnStarted(6000, "考试未开始"),
	/** 考试已结束 */
	ExamFinished(6010, "考试已结束"),

	/** 公开查询链接不存在 */
	QueryNotExist(7000, "该查询链接已失效，请联系项目的发布者"),
	/** 公开查询链接已停止 */
	QueryDisabled(7001, "该查询链接已停止，请联系项目的发布者"),
	/** 公开查询条件不能为空 */
	QueryConditionNull(7002, "查询条件不能为空"),
	/** 查询密码认证失败 */
	QueryPasswordError(7003, "查询密码认证失败"),
	/** 查询条件不存在 */
	QueryConditionNotExist(7004, "查询密码认证失败"),
	/** 公开查询结果不存在 */
	QueryResultNotExist(7010, "没有查询到结果，请确认所填信息正确"),
	/** 公开查询答案更新失败 */
	QueryResultUpdateError(7020, "答案更新失败"),;

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
