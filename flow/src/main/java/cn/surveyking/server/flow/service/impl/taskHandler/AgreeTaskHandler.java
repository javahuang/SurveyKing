package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;
import org.springframework.stereotype.Component;

/**
 * 批准任务
 *
 * @author javahuang
 * @date 2022/1/7
 */
@Component("agreeTaskHandler")
public class AgreeTaskHandler extends AbstractTaskHandler {

	@Override
	public boolean innerProcess(ApprovalTaskRequest request) {
		taskService.complete(request.getTaskId());
		return true;
	}

}
