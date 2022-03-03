package cn.surveyking.server.flow.config;

import cn.surveyking.server.flow.listener.*;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javahuang
 * @date 2021/11/19
 */
@Configuration
@MapperScan("cn.surveyking.server.flow.mapper")
@Slf4j
public class WorkflowConfig {

	@Bean
	@ConditionalOnClass(SpringProcessEngineConfiguration.class)
	public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customizeSpringProcessEngineConfiguration() {
		return processEngineConfiguration -> {
			log.info("Overriding process engine configuration");

			Map<String, List<FlowableEventListener>> instanceListener = new HashMap<>();
			instanceListener.put(FlowableEngineEventType.PROCESS_COMPLETED.name(),
					Collections.singletonList(new ProcessCompletedListener()));
			instanceListener.put(FlowableEngineEventType.ACTIVITY_STARTED.name(),
					Collections.singletonList(new ActivityStartedListener()));
			instanceListener.put(FlowableEngineEventType.PROCESS_STARTED.name(),
					Collections.singletonList(new ProcessStartedListener()));
			instanceListener.put(FlowableEngineEventType.PROCESS_CANCELLED.name(),
					Collections.singletonList(new ProcessCancelledListener()));
			instanceListener.put(FlowableEngineEventType.ENTITY_SUSPENDED.name(),
					Collections.singletonList(new ProcessSuspendedListener()));
			instanceListener.put(FlowableEngineEventType.ENTITY_ACTIVATED.name(),
					Collections.singletonList(new ProcessSuspendedListener()));
			processEngineConfiguration.setTypedEventListeners(instanceListener);
		};
	}

}
