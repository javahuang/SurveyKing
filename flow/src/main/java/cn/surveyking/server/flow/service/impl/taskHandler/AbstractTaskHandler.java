package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.AnswerQuery;
import cn.surveyking.server.domain.dto.AnswerRequest;
import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.flow.constant.FieldPermissionType;
import cn.surveyking.server.flow.constant.FlowApprovalType;
import cn.surveyking.server.flow.constant.FlowConstant;
import cn.surveyking.server.flow.constant.FlowTaskType;
import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import cn.surveyking.server.flow.domain.dto.UpdateFlowOperationUserRequest;
import cn.surveyking.server.flow.domain.model.FlowEntry;
import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import cn.surveyking.server.flow.domain.model.FlowOperation;
import cn.surveyking.server.flow.domain.model.FlowOperationUser;
import cn.surveyking.server.flow.mapper.FlowOperationMapper;
import cn.surveyking.server.flow.service.*;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/12/17
 */
public abstract class AbstractTaskHandler implements TaskHandler {

	@Autowired
	protected AnswerService answerService;

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected FlowInstanceService flowInstanceService;

	@Autowired
	protected FlowEntryNodeService entryNodeService;

	@Autowired
	protected FlowEntryService entryService;

	@Autowired
	protected FlowOperationService flowOperationService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	protected FlowOperationUserService flowOperationUserService;

	@Autowired
	protected RepositoryService repositoryService;

	public abstract boolean innerProcess(ApprovalTaskRequest request);

	@Override
	public void process(ApprovalTaskRequest request) {
		boolean success = innerProcess(request);
		if (!success) {
			return;
		}
		saveOperation(request);
		updateTaskAnswer(request);
	}

	/**
	 * @param target 当前答案
	 * @param source 原始答案
	 * @param fieldPermission 当前答案的数据权限
	 * @return
	 */
	protected LinkedHashMap mergeAnswer(LinkedHashMap target, LinkedHashMap source,
			LinkedHashMap<String, Integer> fieldPermission) {
		LinkedHashMap result = new LinkedHashMap(source);
		if (fieldPermission == null) {
			return target;
		}
		// 原始答案里面过滤掉当前权限允许编辑的答案
		fieldPermission.entrySet().forEach(entry -> {
			String qId = entry.getKey();
			Integer permType = entry.getValue();
			if (permType == FieldPermissionType.editable) {
				result.remove(qId);
			}
		});
		// 合并当前答案
		result.putAll(target);
		return result;
	}

	protected void updateTaskAnswer(ApprovalTaskRequest request) {
		if (request.getAnswer() == null) {
			return;
		}
		FlowEntryNode flowElement = entryNodeService.getById(request.getActivityId());
		AnswerQuery answerQuery = new AnswerQuery();
		answerQuery.setId(request.getAnswerId());
		AnswerView answerView = answerService.getAnswer(answerQuery);
		LinkedHashMap mergedAnswer = mergeAnswer(request.getAnswer(), answerView.getAnswer(),
				flowElement.getFieldPermission());

		AnswerRequest answerRequest = new AnswerRequest();
		answerRequest.setId(request.getAnswerId());
		answerRequest.setAnswer(mergedAnswer);
		answerService.updateAnswer(answerRequest);
	}

	private void saveOperation(ApprovalTaskRequest request) {
		// 更新审批记录为历史审批记录，目的是只有最新操作记录的对应的人才能进行撤回操作
		FlowOperationMapper flowOperationMapper = (FlowOperationMapper) flowOperationService.getBaseMapper();
		flowOperationMapper.updateOperationLatest(request.getProcessInstanceId());

		// 添加新的操作记录
		FlowOperation operation = new FlowOperation();
		operation.setAnswerId(request.getAnswerId());
		operation.setAnswer(request.getAnswer());
		operation.setComment(request.getComment());
		operation.setApprovalType(request.getType());
		operation.setTaskType(FlowTaskType.userTask);
		operation.setTaskId(request.getTaskId());
		operation.setProjectId(request.getProjectId());
		operation.setActivityId(request.getActivityId());
		operation.setNewActivityId(request.getNewActivityId());
		operation.setInstanceId(request.getProcessInstanceId());
		operation.setCreateAt(new Date());
		operation.setCreateBy(SecurityContextUtils.getUserId());
		operation.setLatest(true);
		flowOperationService.save(operation);

		// 更新操作人历史
		// 如果一个人参与了多个流程节点，只显示最近参与的流程节点
		UpdateFlowOperationUserRequest updateFlowOperationUserRequest = new UpdateFlowOperationUserRequest();
		updateFlowOperationUserRequest.setTaskType(FlowTaskType.userTask);
		updateFlowOperationUserRequest.setUserId(SecurityContextUtils.getUserId());
		updateFlowOperationUserRequest.setInstanceId(request.getProcessInstanceId());
		flowOperationMapper.updateOperationUserLatest(updateFlowOperationUserRequest);
		// 更新操作人为最新的操作人
		saveOperationUser(operation);
	}

	private void saveOperationUser(FlowOperation operation) {
		FlowOperationUser user = new FlowOperationUser();
		user.setLatest(true);
		String userId = SecurityContextUtils.getUserId();
		user.setUserId(userId);
		user.setOperationId(operation.getId());
		user.setCreateAt(new Date());
		user.setCreateBy(userId);
		flowOperationUserService.save(user);
	}

	protected Task getCurrentRunningTask(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	protected List<HistoricActivityInstance> getHistoricActivityInstanceList(String processInstanceId) {
		return historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
	}

	protected List<Task> getProcessInstanceActiveTaskList(String processInstanceId) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
	}

	protected List<FlowOperation> getOperations(String processInstanceId) {
		return flowOperationService
				.list(Wrappers.<FlowOperation>lambdaQuery().eq(FlowOperation::getInstanceId, processInstanceId)
						.eq(FlowOperation::getTaskType, FlowTaskType.userTask).orderByDesc(FlowOperation::getCreateAt));
	}

	private StartEvent getStartNode(String projectId) {
		FlowEntry flowEntry = entryService
				.getOne(Wrappers.<FlowEntry>lambdaQuery().eq(FlowEntry::getProjectId, projectId));
		BpmnModel bpmnModel = repositoryService.getBpmnModel(flowEntry.getProcessDefinitionId());
		org.flowable.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
		List<StartEvent> startEvents = process.findFlowElementsOfType(StartEvent.class);
		return startEvents.get(0);
	}

	protected FlowEntry getFlowEntry(String projectId) {
		return entryService.getOne(Wrappers.<FlowEntry>lambdaQuery().eq(FlowEntry::getProjectId, projectId));
	}

	protected boolean rollbackToStartEvent(ApprovalTaskRequest request) {
		if (!request.getNewActivityId().equals(request.getProjectId())) {
			return false;
		}
		// StartEvent event = getStartNode(request.getProjectId());
		String starterActivityId = FlowConstant.STARTER_ACTIVITY_ID;
		String currentActivityId = getProcessInstanceActiveTaskList(request.getProcessInstanceId()).get(0)
				.getTaskDefinitionKey();
		request.setNewActivityId(starterActivityId);
		// runtimeService.createChangeActivityStateBuilder().processInstanceId(request.getProcessInstanceId())
		// .moveActivityIdTo(currentActivityId, starterActivityId);
		// 回滚至开始节点之后，任务自动流程到了下一节点，所以需要暂停任务
		runtimeService.suspendProcessInstanceById(request.getProcessInstanceId());
		return true;
	}

	/**
	 * 将任务流转记录转换成一棵节点树
	 * @param processInstanceId 任务实例 ID
	 * @return 最后一个操作节点
	 */
	protected TaskTreeNode getHistoricTree(String processInstanceId) {
		List<String> approvalTypes = new ArrayList<>();
		approvalTypes.add(FlowApprovalType.SAVE);
		approvalTypes.add(FlowApprovalType.AGREE);
		approvalTypes.add(FlowApprovalType.ROLLBACK);
		approvalTypes.add(FlowApprovalType.REVERT);
		List<FlowOperation> operations = flowOperationService
				.list(Wrappers.<FlowOperation>lambdaQuery().eq(FlowOperation::getInstanceId, processInstanceId)
						.in(FlowOperation::getApprovalType, approvalTypes).orderByAsc(FlowOperation::getCreateAt));
		TaskTreeNode rootNode = null, lastNode = null;
		for (int i = 0; i < operations.size(); i++) {
			FlowOperation operation = operations.get(i);
			TaskTreeNode currNode = new TaskTreeNode();
			currNode.setId(operation.getId());
			currNode.setActivityId(operation.getActivityId());

			if (i == 0) {
				rootNode = currNode;
				lastNode = currNode;
				continue;
			}
			if (lastNode != null && lastNode.getActivityId() != null
					&& lastNode.getActivityId().equals(currNode.getActivityId())) {
				continue;
			}
			if (FlowApprovalType.SAVE.equals(operation.getApprovalType())) {
				lastNode = rootNode;
				continue;
			}
			else if (FlowApprovalType.AGREE.equals(operation.getApprovalType())) {
				currNode.setParent(lastNode);
				if (lastNode.getChildren().stream()
						.noneMatch(node -> node.getActivityId().equals(currNode.getActivityId()))) {
					lastNode.getChildren().add(currNode);
				}
			}
			else if (FlowApprovalType.ROLLBACK.equals(operation.getApprovalType())) {
				lastNode = lastNode.findParentByKey(operation.getNewActivityId());
				continue;
			}
			else if (FlowApprovalType.REVERT.equals(operation.getApprovalType())) {
				// 撤回节点的 taskDefKey 和已完成节点是一致的
			}
			lastNode = currNode;
		}

		return lastNode;
	}

	@Data
	public static class TaskTreeNode {

		private String id;

		private String activityId;

		private TaskTreeNode parent;

		private List<TaskTreeNode> children = new ArrayList<>();

		public TaskTreeNode findParentByKey(String key) {
			if (this.getActivityId().equals(key)) {
				return this;
			}
			if (this.parent == null) {
				return null;
			}
			return this.parent.findParentByKey(key);
		}

		@Override
		public String toString() {
			return "TaskTreeNode{" + "id='" + id + '\'' + ", activityId='" + activityId + '\'' + '}';
		}

	}

}
