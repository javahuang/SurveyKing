package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/4/8
 */
@Data
public class PublicAnswerView {

	/**
	 * 问卷答案
	 */
	private String answerId;

	/**
	 * 考试得分
	 */
	private Double examScore;

	/**
	 * 考试排名
	 */
	private Integer examRanking;

	private LinkedHashMap questionScore;

}
