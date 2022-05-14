package cn.surveyking.server.core.config;

import cn.surveyking.server.core.base.converter.PublicQueryConverter;
import cn.surveyking.server.core.base.converter.UniqueLimitSettingConverter;
import cn.surveyking.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author javahuang
 * @date 2021/10/19
 */
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

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
	}

}
