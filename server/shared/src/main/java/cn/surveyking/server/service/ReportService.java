package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.ReportData;

/**
 * @author javahuang
 * @date 2021/8/3
 */
public interface ReportService {

	ReportData getData(String shortId);

}
