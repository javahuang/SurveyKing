package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.CacheConsts;
import cn.surveyking.server.core.constant.ProjectPartnerTypeEnum;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.ProjectPartnerQuery;
import cn.surveyking.server.domain.dto.ProjectPartnerRequest;
import cn.surveyking.server.domain.dto.ProjectPartnerView;
import cn.surveyking.server.domain.model.ProjectPartner;
import cn.surveyking.server.mapper.ProjectPartnerMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.ProjectPartnerService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2022/1/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ProjectPartnerServiceImpl extends BaseService<ProjectPartnerMapper, ProjectPartner>
		implements ProjectPartnerService {

	private final UserService userService;

	private final CacheManager cacheManager;

	@Override
	public List<ProjectPartnerView> listProjectPartner(ProjectPartnerQuery query) {
		return list(Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, query.getProjectId())
				.in(query.getTypes() != null, ProjectPartner::getType, query.getTypes())
				.eq(query.getStatus() != null, ProjectPartner::getStatus, query.getStatus())
				.orderByAsc(ProjectPartner::getCreateAt)).stream().map(partner -> {
					ProjectPartnerView view = new ProjectPartnerView();
					if (partner.getUserId() != null) {
						view.setUser(userService.loadUserById(partner.getUserId()));
					}
					view.setStatus(partner.getStatus());
					view.setType(partner.getType());
					view.setId(partner.getId());
					view.setUserName(partner.getUserName());
					return view;
				}).collect(Collectors.toList());
	}

	@Override
	public void addProjectPartner(ProjectPartnerRequest request) {
		// 添加答题人
		List<ProjectPartner> existUserIds = list(Wrappers.<ProjectPartner>lambdaQuery()
				.in(request.getUserIds() != null, ProjectPartner::getUserId, request.getUserIds())
				.in(request.getUserNames() != null
						&& ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == request.getType(),
						ProjectPartner::getUserName, request.getUserNames())
				.eq(ProjectPartner::getType, request.getType())
				.eq(ProjectPartner::getProjectId, request.getProjectId()));
		if (ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == request.getType()) {
			saveBatch(request.getUserNames().stream().filter(username -> {
				if (existUserIds.stream().anyMatch(partner -> partner.getUserName().equals(username))) {
					return false;
				}
				return true;
			}).map(userName -> {
				ProjectPartner partner = new ProjectPartner();
				partner.setProjectId(request.getProjectId());
				partner.setUserName(userName);
				partner.setType(request.getType());
				return partner;
			}).collect(Collectors.toList()));
		}
		else {
			saveBatch(request.getUserIds().stream().filter(userId -> {
				if (existUserIds.stream().anyMatch(partner -> partner.getUserId().equals(userId))) {
					return false;
				}
				if (ProjectPartnerTypeEnum.OWNER.getType() == request.getType()
						|| ProjectPartnerTypeEnum.COLLABORATOR.getType() == request.getType()) {
					cacheManager.getCache(CacheConsts.projectPermissionCacheName).evict(userId);
				}
				return true;
			}).map(userId -> {
				ProjectPartner partner = new ProjectPartner();
				partner.setProjectId(request.getProjectId());
				partner.setUserId(userId);
				partner.setType(request.getType());
				return partner;
			}).collect(Collectors.toList()));
		}
	}

	@Override
	public void deleteProjectPartner(ProjectPartnerRequest request) {
		remove(Wrappers.<ProjectPartner>lambdaQuery().in(ProjectPartner::getId, request.getIds()));
	}

	@Override
	public List<String> getProjectPerms() {
		return getBaseMapper().getProjectPerms(SecurityContextUtils.getUserId());
	}

}
