package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.DashboardQuery;
import cn.surveyking.server.domain.dto.DashboardRequest;
import cn.surveyking.server.domain.dto.DashboardView;
import cn.surveyking.server.domain.mapper.DashboardViewMapper;
import cn.surveyking.server.domain.model.Dashboard;
import cn.surveyking.server.mapper.DashboardMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.DashboardService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DashboardServiceImpl extends BaseService<DashboardMapper, Dashboard> implements DashboardService {

	private DashboardViewMapper dashboardViewMapper;

	@Override
	public List<DashboardView> listDashboard(DashboardQuery query) {
		List<Dashboard> dashboardList = list(Wrappers.<Dashboard>lambdaQuery().eq(Dashboard::getType, query.getType())
				.eq(query.getProjectId() != null, Dashboard::getProjectId, query.getProjectId()));
		return dashboardViewMapper.toView(dashboardList);
	}

	@Override
	public void saveDashboard(List<DashboardRequest> request) {

	}

}
