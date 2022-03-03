package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/11/9
 */
@Data
public class FlowNodeSetting {

	/** 是否需要密码 */
	private Boolean passwordRequired;

	/** 密码 */
	private String password;

	/** 允许撤回 */
	private Boolean allowWithdraw;

	/** 是否可见流程日志 */
	private Boolean flowLogVisible;

	/** 允许结束流程 */
	private Boolean allowFinishFlow;

	/** 留言板 */
	private Boolean messageBoardVisible;

	/** 代办转交 */
	private String allowAssignee;

	/** 转交人 */
	private String assigneeTo;

	/** 流程回退 */
	private Boolean allowGoBack;

	/** 1:常规审批 2:逐级审批 */
	private Integer isSequential;

	/** 1:或签 2:会签 */
	private Integer approveType;

}
