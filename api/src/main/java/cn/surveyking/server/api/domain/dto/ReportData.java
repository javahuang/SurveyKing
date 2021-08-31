package cn.surveyking.server.api.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author javahuang
 * @date 2021/8/4
 */
@Data
public class ReportData {

	private Integer total;

	private Map<String, Data> statistics;

	@lombok.Data
	public static class Data {

		private Number min;

		private Number max;

		private Number average;

		private Number sum = 0;

		/** 该问题完成数量 */
		private int total;

	}

}
