package cn.surveyking.server.api.domain.dto;

import cn.surveyking.server.api.domain.model.ProjectSetting;
import lombok.Data;

/**
 * @author javahuang
 * @date 2021/8/25
 */
@Data
public class ProjectView {

	private String id;

	private String shortId;

	private String name;

	private Integer status;

	private SurveySchemaType survey;

	private ProjectSetting setting;

	private Integer total;

	/** 今日答卷数量 */
	private Integer totalOfToday;

	/** 最近更新时间 */
	private Long lastUpdate;

	/** 平均填写时长 */
	private Long averageDuration;

}
