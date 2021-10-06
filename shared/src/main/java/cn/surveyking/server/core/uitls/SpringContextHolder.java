package cn.surveyking.server.core.uitls;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author javahuang
 * @date 2021/8/26
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}

	public static <T> T getBean(Class<T> requiredType) {
		return CONTEXT.getBean(requiredType);
	}

	public static <T> T getBean(String beanName) {
		return (T) CONTEXT.getBean(beanName);
	}

}
