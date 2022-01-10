package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.flow.constant.FlowConstant;
import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
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
	public void innerProcess(ApprovalTaskRequest request) {
		Map<String, Object> variables = new HashMap<>();
		variables.put(FlowConstant.VARIABLE_ANSWER_KEY, request.getAnswerId());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(request.getProjectId(),
				request.getAnswerId(), variables);
		request.setProcessInstanceId(processInstance.getId());
	}

}