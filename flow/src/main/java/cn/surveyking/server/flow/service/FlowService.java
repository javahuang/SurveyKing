package cn.surveyking.server.flow.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.SurveySchema;
import cn.surveyking.server.flow.domain.dto.*;

/**
 * @author javahuang
 * @date 2022/1/5
 */
public interface FlowService {

	/**
	 * 启动前过滤过滤当前问卷 schema
	 * @param userId 当前
	 * @param schema
	 * @return
	 */
	SurveySchema beforeLaunchProcess(String userId, SurveySchema schema);

	/**
	 * 流程设计保存
	 * @param request
	 */
	void saveFlow(FlowEntryRequest request);

	/**
	 * 部署设计部署
	 * @param projectId
	 */
	void deploy(String projectId);

	/**
	 * 完成任务
	 * @param request
	 */
	void approvalTask(ApprovalTaskRequest request);

	/**
	 * 获取流程信息
	 * @param projectId
	 * @return
	 */
	FlowEntryView getFlowEntry(String projectId);

	/**
	 * 获取用户任务列表
	 * @param query
	 * @return
	 */
	PaginationResponse<FlowTaskView> getFlowTasks(FlowTaskQuery query);

	/**
	 * 查询当前任务节点根据权限过滤之后的 schema
	 * @param query
	 * @return
	 */
	SurveySchema loadSchemaByPermission(SchemaQuery query);

}
