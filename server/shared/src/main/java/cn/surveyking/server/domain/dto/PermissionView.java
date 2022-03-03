package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
public class PermissionView {

	private String name;

	private String code;

	public PermissionView(String code) {
		this.code = code;
	}

}
