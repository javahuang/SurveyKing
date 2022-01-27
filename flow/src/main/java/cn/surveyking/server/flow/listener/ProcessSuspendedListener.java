package cn.surveyking.server.flow.listener;

import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.flow.constant.FlowInstanceStatus;
import cn.surveyking.server.flow.domain.model.FlowInstance;
import cn.surveyking.server.flow.service.FlowInstanceService;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;

/**
 * @author javahuang
 * @date 2022/1/23
 */
public class ProcessSuspendedListener implements FlowableEventListener {

	@Override
	public void onEvent(FlowableEvent event) {
		FlowableEntityEventImpl entityEvent = (FlowableEntityEventImpl) event;
		FlowInstanceService flowInstanceService = ContextHelper.getBean(FlowInstanceService.class);
		FlowInstance instance = new FlowInstance();
		instance.setStatus(FlowInstanceStatus.SUSPENDED);
		// 更新当前任务阶段为待申请人完善中
		instance.setApprovalStage(FlowInstanceStatus.getDictStatus(FlowInstanceStatus.SUSPENDED));
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
