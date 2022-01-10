package cn.surveyking.server.flow.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务状态
 *
 * @author javahuang
 * @date 2022/1/6
 */
public final class FlowTaskStatus {

	/**
	 * 已提交。
	 */
	public static final int SUBMITTED = 0;

	/**
	 * 审批中。
	 */
	public static final int APPROVING = 1;

	/**
	 * 被拒绝。
	 */
	public static final int REFUSED = 2;

	/**
	 * 已结束。
	 */
	public static final int FINISHED = 3;

	/**
	 * 提前停止。
	 */
	public static final int STOPPED = 4;

	/**
	 * 已取消。
	 */
	public static final int CANCELLED = 5;

	private static final Map<Object, String> DICT_MAP = new HashMap<>(2);
	static {
		DICT_MAP.put(SUBMITTED, "已提交");
		DICT_MAP.put(APPROVING, "审批中");
		DICT_MAP.put(REFUSED, "被拒绝");
		DICT_MAP.put(FINISHED, "已结束");
		DICT_MAP.put(STOPPED, "提前停止");
		DICT_MAP.put(CANCELLED, "已取消");
	}

	public static String getDictStatus(int status) {
		return DICT_MAP.get(status);
	}

}
