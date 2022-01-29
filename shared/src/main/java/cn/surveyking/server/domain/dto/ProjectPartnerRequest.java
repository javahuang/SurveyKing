package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/28
 */
@Data
public class ProjectPartnerRequest {

	private List<String> userIds;

	private String projectId;

}
