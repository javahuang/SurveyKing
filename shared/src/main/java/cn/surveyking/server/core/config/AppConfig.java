package cn.surveyking.server.core.config;

import cn.surveyking.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;

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
	 * 系统用户初始化
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void initAfterStartup() {
		// userService.init();
	}

}
