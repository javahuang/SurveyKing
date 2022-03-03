package cn.surveyking.server.flow.service.impl.taskHandler;

import cn.surveyking.server.flow.domain.dto.ApprovalTaskRequest;

/**
 * @author javahuang
 * @date 2021/12/17
 */
public interface TaskHandler {

	void process(ApprovalTaskRequest request);

}
