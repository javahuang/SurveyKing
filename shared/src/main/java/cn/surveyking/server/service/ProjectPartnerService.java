package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.ProjectPartnerRequest;
import cn.surveyking.server.domain.dto.ProjectPartnerView;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/28
 */
public interface ProjectPartnerService {

	List<ProjectPartnerView> listProjectPartner(String projectId);

	@CacheEvict(cacheNames = "projectPermissionCache",
			key = "T(cn.surveyking.server.core.uitls.SecurityContextUtils).getUserId()")
	void addProjectPartner(ProjectPartnerRequest request);

	@CacheEvict(cacheNames = "projectPermissionCache",
			key = "T(cn.surveyking.server.core.uitls.SecurityContextUtils).getUserId()")
	void deleteProjectPartner(String projectId, String id);

	@Cacheable(cacheNames = "projectPermissionCache",
			key = "T(cn.surveyking.server.core.uitls.SecurityContextUtils).getUserId()")
	List<String> getProjectPerms();

}
