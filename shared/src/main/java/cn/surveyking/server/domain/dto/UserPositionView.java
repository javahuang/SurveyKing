package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Data
public class UserPositionView {

	private String id;

	private String userId;

	private String orgId;

	private String orgName;

	private String positionId;

	private String positionName;

}
