package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
public class UserTokenView {

	private String userId;

	public UserTokenView() {
	}

	public UserTokenView(String userId) {
		this.userId = userId;
	}

}
