package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.flow.constant.FlowConstant;
import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import cn.surveyking.server.flow.domain.model.FlowEntry;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务发起。
 *
 * @author javahuang
 * @date 2022/1/9
 */
@Component("saveTaskHandler")
public class SaveTaskHandler extends AbstractTaskHandler {

	@Override
	public boolean innerProcess(ApprovalTaskRequest request) {
		Map<String, Object> variables = new HashMap<>();
		variables.put(FlowConstant.VARIABLE_ANSWER_KEY, request.getAnswerId());
		variables.put(FlowConstant.VARIABLE_STARTER_USER, SecurityContextUtils.getUserId());
		FlowEntry entry = getFlowEntry(request.getProjectId());
		// 当前问卷未绑定工作流
		if (entry == null) {
			return false;
		}
		// 表示首次提交
		if (request.getProcessInstanceId() == null) {
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(request.getProjectId(),
					request.getAnswerId(), variables);
			request.setProcessInstanceId(processInstance.getId());
		}
		else {
			// 表示再次提交，首先需要再次激活当前任务
			runtimeService.activateProcessInstanceById(request.getProcessInstanceId());
			// 然后将任务移动到开始节点
			String starterActivityId = FlowConstant.STARTER_ACTIVITY_ID;
			String currentActivityId = getProcessInstanceActiveTaskList(request.getProcessInstanceId()).get(0)
					.getTaskDefinitionKey();
			request.setNewActivityId(starterActivityId);
			runtimeService.createChangeActivityStateBuilder().processInstanceId(request.getProcessInstanceId())
					.moveActivityIdTo(currentActivityId, starterActivityId).changeState();
		}
		return true;
	}

}