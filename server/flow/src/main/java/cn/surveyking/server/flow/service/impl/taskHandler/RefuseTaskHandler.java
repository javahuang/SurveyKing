package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import org.springframework.stereotype.Component;

/**
 * 拒绝之后，直接结束流程
 *
 * @author javahuang
 * @date 2022/1/7
 */
@Component("refuseTaskHandler")
public class RefuseTaskHandler extends AbstractTaskHandler {

	@Override
	public boolean innerProcess(ApprovalTaskRequest request) {
		runtimeService.deleteProcessInstance(request.getProcessInstanceId(), request.getComment());
		return true;
	}

}
