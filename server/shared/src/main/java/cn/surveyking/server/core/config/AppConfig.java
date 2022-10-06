package cn.surveyking.server.core.config;

import cn.surveyking.server.core.base.converter.PublicQueryConverter;
import cn.surveyking.server.core.base.converter.RandomSurveyConverter;
import cn.surveyking.server.core.base.converter.UniqueLimitSettingConverter;
import cn.surveyking.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author javahuang
 * @date 2021/10/19
 */
@Configuration
@EnableAspectJAutoProxy
@EnableAsync
public class AppConfig implements AsyncConfigurer {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 一些初始化操作
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void initAfterStartup() {
		// userService.init();
		DefaultConversionService defaultConversionService = (DefaultConversionService) DefaultConversionService
				.getSharedInstance();
		defaultConversionService.addConverter(new UniqueLimitSettingConverter());
		defaultConversionService.addConverter(new PublicQueryConverter());
		defaultConversionService.addConverter(new RandomSurveyConverter());
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(8);
		executor.setThreadNamePrefix("MyExecutor-");
		executor.initialize();
		return executor;
	}

}
