package cn.surveyking.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

	public static final String USER_ADMIN = "USER_ADMIN";

	private String authority;

}
