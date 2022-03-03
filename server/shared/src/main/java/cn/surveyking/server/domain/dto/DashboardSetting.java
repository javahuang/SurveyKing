package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/28
 */
@Data
public class DashboardSetting {

	private String key;

	private String title;

	/**
	 *
	 */
	private List<GridDataItemTab> tabs;

	/**
	 * 刷新间隔
	 */
	private Integer refreshInterval;

	/**
	 * 组件的 props
	 */
	private LinkedHashMap<String, Object> widgetProps;

	private GridLayout gridLayout;

	@Data
	private static class GridDataItemTab {

		private String key;

		private String title;

	}

	@Data
	private static class GridLayout {

		private String i;

		private Integer x;

		private Integer y;

		private Integer w;

		private Integer h;

		@JsonProperty("static")
		private Boolean _static;

	}

}
