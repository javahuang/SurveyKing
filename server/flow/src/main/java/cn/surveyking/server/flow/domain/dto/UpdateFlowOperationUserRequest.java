package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/1/11
 */
@Data
public class UpdateFlowOperationUserRequest {

	private String instanceId;

	private String userId;

	private Integer taskType;

}
