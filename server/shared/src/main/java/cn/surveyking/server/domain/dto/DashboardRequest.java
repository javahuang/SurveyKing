package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/1/28
 */
@Data
public class DashboardRequest {

	private String id;

	private DashboardSetting setting;

}
