package cn.surveyking.server.flow.domain.dto;

import cn.surveyking.server.domain.dto.DeptView;
import cn.surveyking.server.domain.dto.FileView;
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
	private Integer status;

	private String approvalStage;

	/** 审批类型 */
	private String approvalType;

	/** 任务定义 key */
	private String activityId;

	private Date createAt;

	/** 流程更新时间 */
	private Date updateAt;

	private UserInfo createUser;

	private String processInstanceId;

	private String answerId;

	/**
	 * 字段权限
	 */
	LinkedHashMap<String, Integer> fieldPermission;

	/** 当前实例最新的操作记录 */
	private Boolean latest;

	private LinkedHashMap<String, Object> answer;

	private List<FileView> attachment;

	private List<UserInfo> users;

	private List<DeptView> depts;

}
