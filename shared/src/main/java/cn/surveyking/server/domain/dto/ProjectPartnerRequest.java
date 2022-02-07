package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.AppConsts;
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

	private Integer type = AppConsts.ProjectPartnerType.COLLABORATOR;

}
