package cn.surveyking.server.core.uitls;

import cn.surveyking.server.api.domain.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author javahuang
 * @date 2021/8/31
 */
public class SecurityContextUtils {

	public static User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static String getUsername() {
		return getUser().getUsername();
	}

}
