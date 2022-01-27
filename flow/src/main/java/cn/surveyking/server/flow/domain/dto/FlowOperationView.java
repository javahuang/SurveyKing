package cn.surveyking.server.flow.domain.dto;

import cn.surveyking.server.domain.dto.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/13
 */
@Data
public class FlowOperationView {

	private String id;

	private String activityId;

	private String newActivityId;

	/**
	 * 节点名称
	 */
	private String activityName;

	private String newActivityName;

	private String comment;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createAt;

	private String createBy;

	private UserInfo auditUser;

	private String approvalType;

	private String approvalTypeName;

	/**
	 * 待审批用户
	 */
	private List<UserInfo> waitAuditUserList;

	/**
	 * 抄送用户
	 */
	private List<UserInfo> ccUserList;

}
