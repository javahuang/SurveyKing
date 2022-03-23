package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author javahuang
 * @date 2022/3/23
 */
@Data
public class PublicStatisticsView {

	/**
	 * 总的记录数
	 */
	private int count;

	private Map<String, QuestionStatistics> questionStatistics;

	@Data
	public static class QuestionStatistics {

		/**
		 * 回答了当前问题的记录数
		 */
		private int count;

		List<OptionStatistics> optionStatistics;

		public QuestionStatistics() {
		}

		public QuestionStatistics(int count, List<OptionStatistics> optionStatistics) {
			this.count = count;
			this.optionStatistics = optionStatistics;
		}

	}

	@Data
	public static class OptionStatistics {

		private String optionId;

		private int count;

		private int percentage;

	}

}
