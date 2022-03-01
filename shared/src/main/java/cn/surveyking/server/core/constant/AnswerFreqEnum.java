package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2022/2/27
 */
public enum AnswerFreqEnum {

	only(""),
	/** 小时 */
	hour("0 0 * * * *"),
	/** 天 */
	day("0 0 0 1-31 * *"),
	/** 星期 */
	week("0 0 0 1-31 1-12 1"),
	/** 月 */
	month("0 0 0 1 * *"),
	/** 季度 */
	quarter("0 0 0 1 */3 *"),
	/** 年 */
	year("0 0  0 1 1 *");

	private String cron;

	AnswerFreqEnum(String cron) {
		this.cron = cron;
	}

	public String getCron() {
		return cron;
	}

}
