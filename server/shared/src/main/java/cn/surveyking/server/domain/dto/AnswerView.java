package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class AnswerView {

	private String id;

	private String projectId;

	private LinkedHashMap<String, Object> answer;

	private List<FileView> attachment = new ArrayList<>();

	private List<UserInfo> users;

	private List<DeptView> depts;

	private AnswerMetaInfo metaInfo;

	private Double examScore;

	/**
	 * 考试信息
	 */
	private AnswerExamInfo examInfo;

	/**
	 * 0 暂存 1 已完成
	 */
	private Integer tempSave;

	private Date createAt;

	private String createByName;

	private String createBy;

	private Date updateAt;

	private String updateBy;

}
