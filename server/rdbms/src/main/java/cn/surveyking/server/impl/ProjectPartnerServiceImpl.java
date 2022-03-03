package cn.surveyking.server.impl;

import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.ProjectPartnerRequest;
import cn.surveyking.server.domain.dto.ProjectPartnerView;
import cn.surveyking.server.domain.model.ProjectPartner;
import cn.surveyking.server.mapper.ProjectPartnerMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.ProjectPartnerService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
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

	@Override
	public List<ProjectPartnerView> listProjectPartner(String projectId) {
		return list(Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, projectId)
				.orderByAsc(ProjectPartner::getCreateAt)).stream().map(partner -> {
					ProjectPartnerView view = new ProjectPartnerView();
					view.setUser(userService.loadUserById(partner.getUserId()));
					view.setType(partner.getType());
					view.setId(partner.getId());
					return view;
				}).collect(Collectors.toList());
	}

	@Override
	public void addProjectPartner(ProjectPartnerRequest request) {
		// 过滤掉已存在的用户
		List<String> existUserIds = list(
				Wrappers.<ProjectPartner>lambdaQuery().in(ProjectPartner::getUserId, request.getUserIds())
						.eq(ProjectPartner::getProjectId, request.getProjectId())).stream().map(x -> x.getUserId())
								.collect(Collectors.toList());
		saveBatch(request.getUserIds().stream().filter(userId -> {
			if (existUserIds.contains(userId)) {
				return false;
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

	@Override
	public void deleteProjectPartner(String projectId, String id) {
		remove(Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, projectId)
				.eq(ProjectPartner::getId, id));
	}

	@Override
	public List<String> getProjectPerms() {
		return getBaseMapper().getProjectPerms(SecurityContextUtils.getUserId());
	}

}
