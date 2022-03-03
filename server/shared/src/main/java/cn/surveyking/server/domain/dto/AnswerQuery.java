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

}
