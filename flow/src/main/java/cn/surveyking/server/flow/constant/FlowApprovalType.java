package cn.surveyking.server.flow.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 审批类型
 *
 * @author javahuang
 * @date 2022/1/6
 */
public final class FlowApprovalType {

	/**
	 * 保存。
	 */
	public static final String SAVE = "save";

	/**
	 * 同意。
	 */
	public static final String AGREE = "agree";

	/**
	 * 拒绝，流程直接结束。
	 */
	public static final String REFUSE = "refuse";

	/**
	 * 驳回到之前的某个节点。
	 */
	public static final String ROLLBACK = "rollback";

	/**
	 * 撤销，发起人撤销任务。
	 */
	public static final String REVERT = "revert";

	/**
	 * 指派。
	 */
	public static final String TRANSFER = "transfer";

	/**
	 * 多实例会签。
	 */
	public static final String MULTI_SIGN = "multi_sign";

	/**
	 * 会签同意。
	 */
	public static final String MULTI_AGREE = "multi_agree";

	/**
	 * 会签拒绝。
	 */
	public static final String MULTI_REFUSE = "multi_refuse";

	/**
	 * 会签弃权。
	 */
	public static final String MULTI_ABSTAIN = "multi_abstain";

	/**
	 * 多实例加签。
	 */
	public static final String MULTI_CONSIGN = "multi_consign";

	/**
	 * 中止。
	 */
	public static final String STOP = "stop";

	/**
	 * 待办
	 */
	public static final String TODO = "todo";

	public static final Map<Object, String> DICT_MAP = new HashMap<>(2);
	static {
		DICT_MAP.put(SAVE, "保存");
		DICT_MAP.put(AGREE, "同意");
		DICT_MAP.put(REFUSE, "拒绝");
		DICT_MAP.put(TRANSFER, "指派");
		DICT_MAP.put(MULTI_SIGN, "多实例会签");
		DICT_MAP.put(MULTI_AGREE, "会签同意");
		DICT_MAP.put(MULTI_REFUSE, "会签拒绝");
		DICT_MAP.put(MULTI_ABSTAIN, "会签弃权");
		DICT_MAP.put(MULTI_CONSIGN, "多实例加签");
		DICT_MAP.put(STOP, "中止");
		DICT_MAP.put(TODO, "待处理");
	}

}
