package cn.surveyking.server.flow.domain.dto;

import cn.surveyking.server.domain.dto.Attachment;
import cn.surveyking.server.domain.dto.UserInfo;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@Data
public class FlowTaskView {

	private String id;

	private String projectId;

	/** 任务状态 */
	private String taskStatus;

	/** 任务定义 key */
	private String activityId;

	private Date createAt;

	/** 流程更新时间 */
	private Date updateAt;

	private UserInfo createUser;

	private String processInstanceId;

	private String answerId;

	private LinkedHashMap<String, Object> answer;

	private List<Attachment> attachment;

}
