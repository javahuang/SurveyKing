package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AnswerQuery extends PageQuery {

	private String id;

	private String projectId;

	private List<String> ids;

	private String ip;

	private String cookie;

	private Date startTime;

	private Date endTime;

	private String createBy;

	/** 获取最近一份答案 */
	private Boolean latest;

	private boolean rankEnabled;

	/**
	 * 根据选项答案查询
	 */
	private String valueQuery;

}
