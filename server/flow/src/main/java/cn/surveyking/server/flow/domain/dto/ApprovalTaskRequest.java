package cn.surveyking.server.flow.domain.dto;

import cn.surveyking.server.domain.dto.AnswerRequest;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@Data
public class ApprovalTaskRequest {

	/** 当前流程表单答案 */
	private LinkedHashMap answer;

	private String comment;

	private List<AnswerRequest> attachment;

	private String projectId;

	/** 答案id */
	private String answerId;

	/** 流程委托 */
	private String assignee;

	private String activityId;

	/** 驳回到指定节点 */
	private String newActivityId;

	private String id;

	/** 任务id */
	private String taskId;

	private String processInstanceId;

	private String type;

}
