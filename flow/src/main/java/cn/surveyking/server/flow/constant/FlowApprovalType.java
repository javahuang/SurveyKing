package cn.surveyking.server.flow.constant;

/**
 * 工作流任务触发 BUTTON。
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
	 * 驳回。
	 */
	public static final String REJECT = "reject";

	/**
	 * 撤销，发起人撤销任务。
	 */
	public static final String REVOKE = "revoke";

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

}
