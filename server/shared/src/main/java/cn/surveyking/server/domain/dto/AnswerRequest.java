package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class AnswerRequest {

	private String id;

	/**
	 * 公开查询 id
	 */
	private String queryId;

	private String projectId;

	private LinkedHashMap<String, Object> answer;

	private AnswerMetaInfo metaInfo;

	/**
	 * 0 暂存 1 已完成
	 */
	private Integer tempSave;

	private AnswerExamInfo examInfo;

	private List<String> ids;

	private String whitelistName;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 修改人
	 */
	private String updateBy;

}
