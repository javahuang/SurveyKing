/*
 * Copyright (c) 2015-2020, www.dibo.ltd (service@dibo.ltd).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cn.surveyking.server.core.uitls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Spring上下文帮助类
 *
 * @author mazc@dibo.ltd
 * @version 2.0
 * @date 2019/01/01
 */
@Component
@Lazy(false)
@Slf4j
public class ContextHelper implements ApplicationContextAware {

	/***
	 * ApplicationContext上下文
	 */
	private static ApplicationContext APPLICATION_CONTEXT = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		APPLICATION_CONTEXT = applicationContext;
	}

	/***
	 * 获取ApplicationContext上下文
	 */
	public static ApplicationContext getApplicationContext() {
		if (APPLICATION_CONTEXT == null) {
			APPLICATION_CONTEXT = ContextLoader.getCurrentWebApplicationContext();
		}
		if (APPLICATION_CONTEXT == null) {
			log.warn("无法获取ApplicationContext，请确保ComponentScan扫描路径包含com.diboot包路径，并在Spring初始化之后调用接口!");
		}
		return APPLICATION_CONTEXT;
	}

	/***
	 * 根据beanId获取Bean实例
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId) {
		return getApplicationContext().getBean(beanId);
	}

	/***
	 * 获取指定类型的单个Bean实例
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		try {
			return getApplicationContext().getBean(clazz);
		}
		catch (Exception e) {
			log.debug("instance not found: {}", clazz.getSimpleName());
			return null;
		}
	}

	/***
	 * 获取指定类型的全部实现类
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getBeans(Class<T> type) {
		Map<String, T> map = getApplicationContext().getBeansOfType(type);
		if (map == null || map.size() == 0) {
			return null;
		}
		List<T> beanList = new ArrayList<>(map.size());
		beanList.addAll(map.values());
		return beanList;
	}

	/***
	 * 根据注解获取beans
	 * @param annotationType
	 * @return
	 */
	public static List<Object> getBeansByAnnotation(Class<? extends Annotation> annotationType) {
		Map<String, Object> map = getApplicationContext().getBeansWithAnnotation(annotationType);
		if (map == null || map.size() == 0) {
			return null;
		}
		List<Object> beanList = new ArrayList<>();
		beanList.addAll(map.values());
		return beanList;
	}

}