package cn.surveyking.server.core.uitls;

import cn.surveyking.server.api.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author javahuang
 * @date 2021/8/31
 */
public class SecurityContextUtils {

    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new User("guest");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return new User("guest");
    }

    public static String getUsername() {
        return getUser().getUsername();
    }

}
