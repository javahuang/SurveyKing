package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/5/3
 */
@Data
public class PickRepoQuestionRequest {

	/**
	 * 关联题库ID
	 */
	private String repoId;

	/**
	 * 问题类型
	 */
	private List<SurveySchema.QuestionType> types = new ArrayList<>();

	/**
	 * 问题标签
	 */
	private List<String> tags = new ArrayList<>();

	/**
	 * 选中问题数量
	 */
	private Integer questionsNum;

	/**
	 * 每题分值
	 */
	private Double examScore;

}
