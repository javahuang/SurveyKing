package cn.surveyking.server.core.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Component
public class PasswordEncoder extends BCryptPasswordEncoder {

}
