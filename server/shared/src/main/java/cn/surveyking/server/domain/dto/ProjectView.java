package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/25
 */
@Data
public class ProjectView {

	private String id;

	private String name;

	private ProjectModeEnum mode;

	private Integer status;

	private SurveySchema survey;

	private ProjectSetting setting;

	private Long total;

	/** 今日答卷数量 */
	private Integer totalOfToday;

	/** 最近更新时间 */
	private Long lastUpdate;

	/** 平均填写时长 */
	private Long averageDuration;

	private Date createAt;

	private Date updateAt;

	private String parentId;

}
