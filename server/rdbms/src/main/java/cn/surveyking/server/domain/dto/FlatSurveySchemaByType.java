package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/7
 */
@Data
public class FlatSurveySchemaByType {

	/**
	 * 原始 schema
	 */
	List<SurveySchema> schemaDataTypes;

	/**
	 * 用户题
	 */
	List<SurveySchema> userQuestions;

	/**
	 * 部门题
	 */
	List<SurveySchema> deptQuestions;

	/**
	 * 文件题
	 */
	List<SurveySchema> fileQuestions;

}
