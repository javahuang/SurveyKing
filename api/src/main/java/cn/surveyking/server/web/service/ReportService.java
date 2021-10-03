package cn.surveyking.server.web.service;

import cn.surveyking.server.web.domain.dto.ReportData;

/**
 * @author javahuang
 * @date 2021/8/3
 */
public interface ReportService {

	ReportData getData(String shortId);

}
