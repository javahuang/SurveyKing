package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.AnswerQuery;
import cn.surveyking.server.domain.dto.AnswerRequest;
import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.flow.constant.FieldPermissionType;
import cn.surveyking.server.flow.constant.FlowApprovalType;
import cn.surveyking.server.flow.constant.FlowTaskType;
import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import cn.surveyking.server.flow.domain.model.FlowOperation;
import cn.surveyking.server.flow.domain.model.FlowOperationIdentitylink;
import cn.surveyking.server.flow.service.FlowEntryNodeService;
import cn.surveyking.server.flow.service.FlowInstanceService;
import cn.surveyking.server.flow.service.FlowOperationIdentitylinkService;
import cn.surveyking.server.flow.service.FlowOperationService;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;
import org.flowable.engine.HistoryService;
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
	protected FlowOperationService flowOperationService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	protected FlowOperationIdentitylinkService flowOperationIdentitylinkService;

	public abstract void innerProcess(ApprovalTaskRequest request);

	@Override
	public void process(ApprovalTaskRequest request) {
		innerProcess(request);
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
		FlowEntryNode flowElement = entryNodeService
				.getOne(Wrappers.<FlowEntryNode>lambdaQuery().eq(FlowEntryNode::getActivityId, request.getActivityId())
						.eq(FlowEntryNode::getProjectId, request.getProjectId()));

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
		FlowOperation operation = new FlowOperation();
		operation.setAnswer(request.getAnswer());
		operation.setComment(request.getComment());
		operation.setApprovalType(request.getType());
		operation.setTaskType(FlowTaskType.userTask);
		operation.setCreateAt(new Date());
		operation.setProjectId(request.getProjectId());
		operation.setProcessInstanceId(request.getProcessInstanceId());

		operation.setCreateBy(SecurityContextUtils.getUserId());
		flowOperationService.save(operation);
		saveOperationIdentityLink(operation);
	}

	private void saveOperationIdentityLink(FlowOperation operation) {
		FlowOperationIdentitylink link = new FlowOperationIdentitylink();
		link.setCreateAt(new Date());
		String userId = SecurityContextUtils.getUserId();
		link.setCreateBy(userId);
		link.setOperationId(operation.getId());
		link.setUserId(userId);
		flowOperationIdentitylinkService.save(link);
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
				.list(Wrappers.<FlowOperation>lambdaQuery().eq(FlowOperation::getProcessInstanceId, processInstanceId)
						.eq(FlowOperation::getTaskType, FlowTaskType.userTask).orderByDesc(FlowOperation::getCreateAt));
	}

	protected TaskTreeNode getHistoricTree(String processInstanceId) {
		List<String> approvalTypes = new ArrayList<>();
		approvalTypes.add(FlowApprovalType.SAVE);
		approvalTypes.add(FlowApprovalType.AGREE);
		approvalTypes.add(FlowApprovalType.REJECT);
		approvalTypes.add(FlowApprovalType.REVOKE);
		List<FlowOperation> operations = flowOperationService
				.list(Wrappers.<FlowOperation>lambdaQuery().eq(FlowOperation::getProcessInstanceId, processInstanceId)
						.in(FlowOperation::getApprovalType, approvalTypes).orderByAsc(FlowOperation::getCreateAt));
		TaskTreeNode rootNode = null, lastNode = null;
		TaskTreeNode result = null;
		for (int i = 0; i < operations.size(); i++) {
			FlowOperation operation = operations.get(i);
			TaskTreeNode currNode = new TaskTreeNode();
			currNode.setId(operation.getId());
			currNode.setTaskDefKey(operation.getActivityId());

			if (i == 0) {
				rootNode = currNode;
				lastNode = currNode;
				continue;
			}
			if (lastNode.getTaskDefKey().equals(currNode.getTaskDefKey())) {
				continue;
			}
			if (FlowApprovalType.SAVE.equals(operation.getApprovalType())) {
				lastNode = rootNode;
				continue;
			}
			else if (FlowApprovalType.AGREE.equals(operation.getApprovalType())) {
				currNode.setParent(lastNode);
				if (lastNode.getChildren().stream()
						.noneMatch(node -> node.getTaskDefKey().equals(currNode.getTaskDefKey()))) {
					lastNode.getChildren().add(currNode);
				}
			}
			else if (FlowApprovalType.REJECT.equals(operation.getApprovalType())) {
				lastNode = lastNode.findParentByKey(operation.getActivityId());
				continue;
			}
			else if (FlowApprovalType.REVOKE.equals(operation.getApprovalType())) {
				// 撤回节点的 taskDefKey 和已完成节点是一致的
			}
			lastNode = currNode;
		}

		return lastNode;
	}

	@Data
	protected static class TaskTreeNode {

		private String id;

		private String taskDefKey;

		private TaskTreeNode parent;

		private List<TaskTreeNode> children = new ArrayList<>();

		public TaskTreeNode findParentByKey(String key) {
			if (this.parent == null) {
				return null;
			}
			if (this.getTaskDefKey().equals(key)) {
				return this;
			}
			return this.parent.findParentByKey(key);
		}

	}

}
