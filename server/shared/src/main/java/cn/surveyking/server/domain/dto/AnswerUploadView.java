package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/5/21
 */
@Data
public class AnswerUploadView {

	private String projectId;

	private SurveySchema schema;

}
