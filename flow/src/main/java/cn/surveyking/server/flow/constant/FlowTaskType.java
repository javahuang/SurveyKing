package cn.surveyking.server.flow.constant;

/**
 * 流程任务类型
 *
 * @author javahuang
 * @date 2022/1/6
 */
public final class FlowTaskType {

	/** 发起任务 */
	public static final int starter = 1;

	/** 用户任务 */
	public static final int userTask = 2;

	/** 抄送 */
	public static final int copyTo = 3;

	/** 邮件任务 */
	public static final int mail = 4;

	/** 短信任务 */
	public static final int sms = 5;

	/** api调用任务 */
	public static final int http = 6;

}
