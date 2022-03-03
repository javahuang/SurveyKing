package cn.surveyking.server.flow.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.SurveySchema;
import cn.surveyking.server.flow.domain.dto.*;
import cn.surveyking.server.flow.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@RestController
@RequestMapping("${api.prefix}/workflow")
@RequiredArgsConstructor
public class FlowApi {

	private final FlowService flowService;

	@GetMapping("/getFlow")
	public FlowEntryView getFlowEntry(String projectId) {
		return flowService.getFlowEntry(projectId);
	}

	@PostMapping("/saveFlow")
	public void saveFlow(@RequestBody FlowEntryRequest request) {
		flowService.saveFlow(request);
	}

	@PostMapping("/deploy")
	public void deploy(String projectId) {
		flowService.deploy(projectId);
	}

	@GetMapping("/getAuditRecord")
	public List<FlowOperationView> getAuditRecord(String processInstanceId) {
		return flowService.getAuditRecord(processInstanceId);
	}

	// 获取当前用户的任务列表
	@GetMapping("/getFlowTasks")
	public PaginationResponse<FlowTaskView> getFlowTasks(FlowTaskQuery query) {
		return flowService.getFlowTasks(query);
	}

	/**
	 * 加载问卷 schema
	 * @param query
	 * @return
	 */
	@GetMapping("/loadSchema")
	public SurveySchema loadSchema(SchemaQuery query) {
		return flowService.loadSchemaByPermission(query);
	}

	/**
	 * 获取任务信息
	 */
	@GetMapping("/getTaskInfo")
	public void getTaskInfo() {

	}

	/**
	 * 任务回退
	 */
	@GetMapping("/getRevertNodes")
	public List<RevokeView> getRevertNodes(String processInstanceId) {
		return flowService.getRevertNodes(processInstanceId);
	}

	@PostMapping("/approvalTask")
	public void approvalTask(@RequestBody ApprovalTaskRequest request) {
		flowService.approvalTask(request);
	}

	@GetMapping("/statics")
	public FlowStaticsView statics() {
		return flowService.statics();
	}

}
