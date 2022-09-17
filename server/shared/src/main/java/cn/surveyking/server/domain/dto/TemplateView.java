package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Data
public class TemplateView {

	private String id;

	private String name;

	private String questionType;

	private SurveySchema template;

	private ProjectModeEnum mode;

	private String[] tag;

	private String category;

	/**
	 * 只有所有者才能对模板进行删除操作
	 */
	private boolean owner;

	private String previewUrl;

	private Date createAt;

	private String repoName;

	private String repoId;

}
