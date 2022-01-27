package cn.surveyking.server.core.uitls;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.domain.dto.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author javahuang
 * @date 2021/8/31
 */
public class SecurityContextUtils {

	public static UserInfo getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return new UserInfo(AppConsts.ANONYMOUS_USER_ID);
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserInfo) {
			return (UserInfo) principal;
		}
		return new UserInfo(AppConsts.ANONYMOUS_USER_ID);
	}

	public static String getUsername() {
		return getUser().getUsername();
	}

	public static String getUserId() {
		return getUser().getUserId();
	}

	public static boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getAuthorities().stream().filter(s -> AppConsts.ROLE_ADMIN.equals(s.getAuthority()))
				.count() > 0;
	}

}
