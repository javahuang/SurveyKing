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

	private String deptId;

	private String deptName;

	private String positionId;

	private String positionName;

}
