package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.DashboardQuery;
import cn.surveyking.server.domain.dto.DashboardRequest;
import cn.surveyking.server.domain.dto.DashboardView;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/28
 */
public interface DashboardService {

	List<DashboardView> listDashboard(DashboardQuery query);

	void saveDashboard(List<DashboardRequest> request);

}
