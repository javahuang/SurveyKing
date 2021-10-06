package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
public class UserView implements UserDetails, Serializable {

	private String id;

	private String username;

	private String fullName;

	private Integer status;

	@JsonIgnore
	private String password;

	private Boolean enabled;

	private Set<Role> authorities = new HashSet<>();

	public UserView() {
	}

	public UserView(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return this.status != null && this.status == 1;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

}
