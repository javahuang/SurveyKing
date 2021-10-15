package cn.surveyking.server.core.security;

import cn.surveyking.server.core.uitls.ContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 提取所有的 PreAuthorize 注解值
 *
 * @author javahuang
 * @date 2021/10/12
 */
@Slf4j
public class PreAuthorizeAnnotationExtractor {

	private static Set<String> permissions = null;

	/**
	 * 提取所有的权限定义
	 * @return
	 */
	public static Set<String> extractAllApiPermissions() {
		List<Object> controllerList = ContextHelper.getBeansByAnnotation(RestController.class);
		if (permissions == null) {
			permissions = new LinkedHashSet<>();
			extractApiPermissions(controllerList);
		}
		return permissions;
	}

	private static void extractApiPermissions(List<Object> controllerList) {
		for (Object obj : controllerList) {
			Class controllerClass = AopProxyUtils.ultimateTargetClass(obj);
			String title = null;
			// 提取类信息
			String codePrefix = null;
			// 注解
			PreAuthorize bindPermission = AnnotationUtils.findAnnotation(controllerClass, PreAuthorize.class);
			if (bindPermission != null) {
				// 当前资源权限
				parseAnnotationValue(bindPermission.value());
			}
			extractAnnotationMethods(controllerClass, PreAuthorize.class).forEach(method -> {
				PreAuthorize methodBindPermission = AnnotationUtils.getAnnotation(method, PreAuthorize.class);
				parseAnnotationValue(methodBindPermission.value());
			});
		}
	}

	/**
	 * 如 @PreAuthorize("hasAuthority='answer:list'")
	 * @param value
	 */
	private static void parseAnnotationValue(String value) {
		if (StringUtils.isBlank(value) || !StringUtils.contains(value, "hasAuthority")) {
			return;
		}
		String authorityCode = StringUtils.substringBetween(value, "'");
		if (StringUtils.isNotBlank(authorityCode)) {
			permissions.add(authorityCode.trim());
		}
		else {
			log.warn("检测到权限码未配置 {}", value);
		}

	}

	/**
	 * 获取类中所有包含注解的方法（包含父类中）
	 * @param clazz
	 * @return
	 */
	public static List<Method> extractAnnotationMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		List<Method> methodList = new ArrayList<>();
		while (clazz != null) {
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null) {
				// 被重写属性，以子类override的为准
				Arrays.stream(methods).forEach((method) -> {
					if (AnnotationUtils.getAnnotation(method, annotationClass) != null) {
						methodList.add(method);
					}
				});
			}
			clazz = clazz.getSuperclass();
		}
		return methodList;
	}

}
