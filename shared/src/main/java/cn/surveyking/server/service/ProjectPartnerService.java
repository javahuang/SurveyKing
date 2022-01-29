package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.ProjectPartnerRequest;
import cn.surveyking.server.domain.dto.ProjectPartnerView;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/28
 */
public interface ProjectPartnerService {

	List<ProjectPartnerView> listProjectPartner(String projectId);

	void addProjectPartner(ProjectPartnerRequest request);

	void deleteProjectPartner(String id);

}
