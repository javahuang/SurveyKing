package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/5/15
 */
@Data
public class PublicQueryVerifyView {

	/**
	 * 查看权限，是否需要密码查看
	 */
	private String password;

	private SurveySchema schema;

}
