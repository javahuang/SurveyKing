package cn.surveyking.server.api.service;

import cn.surveyking.server.api.domain.dto.ReportData;

/**
 * @author javahuang
 * @date 2021/8/3
 */
public interface ReportService {

	ReportData getData(String shortId);

}
