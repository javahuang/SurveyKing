package cn.surveyking.server.flow.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.SurveySchema;
import cn.surveyking.server.flow.domain.dto.*;
import cn.surveyking.server.flow.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

	// 获取当前用户的任务列表
	@GetMapping("/getFlowTasks")
	public PaginationResponse<FlowTaskView> getFlowTasks(FlowTaskQuery query) {
		return flowService.getFlowTasks(query);
	}

	@GetMapping("/loadSchema")
	public SurveySchema loadSchema(SchemaQuery query) {
		return flowService.loadSchemaByPermission(query);
	}

	@PostMapping("/deploy")
	public void deploy(String projectId) {
		flowService.deploy(projectId);
	}

	// 获取任务信息
	@GetMapping("/getTaskInfo")
	public void getTaskInfo() {

	}

	@PostMapping("/approvalTask")
	public void approvalTask(@RequestBody ApprovalTaskRequest request) {
		flowService.approvalTask(request);
	}

}
