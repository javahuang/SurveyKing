package cn.surveyking.server.core.annotation;

import cn.surveyking.server.core.constant.AppConsts;

import java.lang.annotation.*;

/**
 *
 * @author javahuang
 * @date 2022/1/30
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableDataPerm {

	String key() default "";

	/**
	 * 权限类型，根据项目来区分权限
	 * @return
	 */
	String permType() default AppConsts.PermType.PROJECT;

	/**
	 * 默认通过 url 来获取
	 * @return
	 */
	String permFrom() default "";

}
