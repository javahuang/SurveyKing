package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

/**
 * 驳回任务 主动驳回当前的待办任务，只用当前待办任务的指派人或者候选者才能完成该操作。
 *
 * @author javahuang
 * @date 2022/1/7
 */
@Component("rejectTaskHandler")
@Slf4j
public class RejectTaskHandler extends AbstractTaskHandler {

	@Override
	public void innerProcess(ApprovalTaskRequest request) {
		Task task = getCurrentRunningTask(request.getTaskId());
		String currentActivityId = task.getTaskDefinitionKey();
		String newActivityId = request.getNewActivityId();

		TaskTreeNode taskTreeNode = getHistoricTree(task.getProcessInstanceId());
		if (StringUtils.isEmpty(request.getNewActivityId())) {
			// 如果未指定，默认驳回到上一个节点
			newActivityId = taskTreeNode.getParent().getTaskDefKey();
			request.setActivityId(newActivityId);
		}
		if (StringUtils.isNotBlank(newActivityId)) {
			log.info("驳回任务 {} -> {}", currentActivityId, newActivityId);
			runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
					.moveActivityIdTo(currentActivityId, newActivityId).changeState();
		}

	}

}
