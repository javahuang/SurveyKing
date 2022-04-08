package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/4/8
 */
@Data
public class AnswerExamInfo {

	/**
	 * 问题分值
	 */
	private LinkedHashMap<String, Double> questionScore;

}
