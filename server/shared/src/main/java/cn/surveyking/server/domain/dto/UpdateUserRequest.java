package cn.surveyking.server.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
public class UpdateUserRequest {

	@NotBlank
	private String fullName;

	private Set<String> authorities;

}
