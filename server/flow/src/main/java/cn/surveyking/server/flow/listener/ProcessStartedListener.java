package cn.surveyking.server.flow.listener;

import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.flow.constant.FlowConstant;
import cn.surveyking.server.flow.constant.FlowInstanceStatus;
import cn.surveyking.server.flow.domain.model.FlowInstance;
import cn.surveyking.server.flow.service.FlowInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.Date;

/**
 * 流程开始时，添加流程实例
 *
 * @author javahuang
 * @date 2022/1/10
 */
@Slf4j
public class ProcessStartedListener implements FlowableEventListener {

	@Override
	public void onEvent(FlowableEvent event) {
		FlowableProcessStartedEventImpl entityEvent = (FlowableProcessStartedEventImpl) event;
		ExecutionEntityImpl entity = (ExecutionEntityImpl) entityEvent.getEntity();
		// 添加流程实例
		FlowInstance instance = new FlowInstance();
		String answerId = (String) entityEvent.getVariables().get(FlowConstant.VARIABLE_ANSWER_KEY);
		instance.setAnswerId(answerId);
		instance.setProjectId(entity.getProcessDefinitionKey());
		instance.setId(entity.getProcessInstanceId());
		instance.setStatus(FlowInstanceStatus.APPROVING);
		instance.setCreateAt(new Date());
		instance.setCreateBy(SecurityContextUtils.getUserId());
		FlowInstanceService flowInstanceService = ContextHelper.getBean(FlowInstanceService.class);
		flowInstanceService.save(instance);
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
