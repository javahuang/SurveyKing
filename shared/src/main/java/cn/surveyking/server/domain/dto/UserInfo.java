package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
public class UserInfo implements UserDetails, Serializable {

	/** 用户id */
	private String userId;

	/** 用户名 */
	private String name;

	private Boolean enabled;

	@JsonIgnore
	private Integer status;

	/** 登录账号 */
	@JsonIgnore
	private String username;

	@JsonIgnore
	private String password;

	@JsonIgnore
	private Set<GrantedAuthority> authorities = new HashSet<>();

	private List<String> authorityList;

	public UserInfo() {
	}

	public UserInfo(String username) {
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
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public List<String> getAuthorityList() {
		if (this.authorities == null) {
			return null;
		}
		return this.authorities.stream().map(x -> x.getAuthority()).collect(Collectors.toList());
	}

	public UserInfo simpleMode() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(this.getUserId());
		userInfo.setName(this.getName());
		userInfo.setAuthorityList(null);
		return userInfo;
	}

}
