package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import cn.surveyking.server.flow.domain.model.FlowOperation;
import cn.surveyking.server.flow.exception.FlowableRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 撤回当前用户提交的，但是尚未被审批的待办任务。只有已办任务的指派人才能完成该操作。
 *
 * @author javahuang
 * @date 2022/1/7
 */
@Component("revokeTaskHandler")
@Slf4j
public class RevokeTaskHandler extends AbstractTaskHandler {

	@Override
	public void innerProcess(ApprovalTaskRequest request) {
		if (!canRevoke(request.getTaskId(), request.getProcessInstanceId())) {
			throw new FlowableRuntimeException("当前节点不能进行驳回操作");
		}
		getHistoricTree(request.getProcessInstanceId());
		// TODO: 会签撤回
		List<Task> tasks = getProcessInstanceActiveTaskList(request.getProcessInstanceId());
		runtimeService.createChangeActivityStateBuilder().processInstanceId(request.getProcessInstanceId())
				.moveActivityIdTo(tasks.get(0).getTaskDefinitionKey(), request.getActivityId()).changeState();
	}

	/**
	 * 只有最近一条的操作记录是自己才能进行驳回操作
	 * @param taskId
	 * @param processInstanceId
	 * @return
	 */
	public boolean canRevoke(String taskId, String processInstanceId) {
		List<FlowOperation> operationList = getOperations(processInstanceId);
		if (operationList.size() > 0 && taskId.equals(operationList.get(0).getTaskId())) {
			return true;
		}
		return false;
	}

}
