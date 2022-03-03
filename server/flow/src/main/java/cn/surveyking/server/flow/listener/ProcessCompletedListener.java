package cn.surveyking.server.flow.listener;

import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.flow.constant.FlowInstanceStatus;
import cn.surveyking.server.flow.domain.model.FlowInstance;
import cn.surveyking.server.flow.service.FlowInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableProcessEventImpl;

/**
 * 流程实例完成之后，更新 instance 状态
 *
 * @author javahuang
 * @date 2022/1/6
 */
@Slf4j
public class ProcessCompletedListener implements FlowableEventListener {

	@Override
	public void onEvent(FlowableEvent event) {
		FlowableProcessEventImpl entityEvent = (FlowableProcessEventImpl) event;
		FlowInstanceService flowInstanceService = ContextHelper.getBean(FlowInstanceService.class);
		FlowInstance instance = new FlowInstance();
		instance.setStatus(FlowInstanceStatus.FINISHED);
		instance.setApprovalStage(FlowInstanceStatus.getDictStatus(FlowInstanceStatus.FINISHED));
		instance.setId(entityEvent.getProcessInstanceId());
		flowInstanceService.updateById(instance);
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

	@Override
	public boolean isFireOnTransactionLifecycleEvent() {
		return false;
	}

	@Override
	public String getOnTransaction() {
		return null;
	}

}
