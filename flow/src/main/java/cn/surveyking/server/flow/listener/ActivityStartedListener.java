package cn.surveyking.server.flow.listener;

import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.flow.constant.FlowTaskStatus;
import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import cn.surveyking.server.flow.domain.model.FlowInstance;
import cn.surveyking.server.flow.service.FlowEntryNodeService;
import cn.surveyking.server.flow.service.FlowInstanceService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.FlowableActivityEvent;

/**
 * 节点开始时，更新任务实例的当前所处任务阶段字段
 *
 * @author javahuang
 * @date 2022/1/9
 */
@Slf4j
public class ActivityStartedListener implements FlowableEventListener {

	@Override
	public void onEvent(FlowableEvent event) {
		FlowableActivityEvent activityEvent = (FlowableActivityEvent) event;
		System.out.println(activityEvent.getActivityId());
		if ("userTask".equals(activityEvent.getActivityType())) {
			String processInstanceId = activityEvent.getProcessInstanceId();
			String taskDefKey = activityEvent.getActivityId();
			FlowInstanceService flowInstanceService = ContextHelper.getBean(FlowInstanceService.class);
			FlowEntryNodeService flowEntryNodeService = ContextHelper.getBean(FlowEntryNodeService.class);

			FlowInstance flowInstance = new FlowInstance();
			flowInstance.setId(processInstanceId);
			FlowEntryNode flowEntryNode = flowEntryNodeService
					.getOne(Wrappers.<FlowEntryNode>lambdaQuery().eq(FlowEntryNode::getActivityId, taskDefKey));
			String approvalStage = flowEntryNode.getName();
			if (StringUtils.isBlank(approvalStage)) {
				approvalStage = FlowTaskStatus.getDictStatus(FlowTaskStatus.APPROVING);
			}
			flowInstance.setApprovalStage(approvalStage);
			flowInstance.setStatus(FlowTaskStatus.APPROVING);
			flowInstanceService.updateById(flowInstance);
		}
	}

	@Override
	public boolean isFailOnException() {
		return true;
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
