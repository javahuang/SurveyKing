package cn.surveyking.server.core.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author javahuang
 * @date 2022/2/8
 */
@Component("deptKeyGenerator")
public class DeptKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		if (params[0] == null) {
			return "all";
		}
		return "some";
	}

}
