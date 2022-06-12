package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/1/28
 */
@Data
public class ProjectPartnerView {

	private String id;

	private Integer type;

	private UserInfo user;

	private String userName;

	private Integer status;

}
