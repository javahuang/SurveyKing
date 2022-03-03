package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@Data
public class FlowEntryNodeView {

	private String id;

	private String name;

	private String flowId;

	private String projectId;

	/** send/mail/sms/http */
	private String flowType;

	/** 字段权限 0隐藏 1读 2写 */
	private LinkedHashMap<String, Integer> fieldPermission;

	private FlowNodeSetting setting;

	private String[] identity;

	private String expression;

}
