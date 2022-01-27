package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
public class ProjectSetting {

	private AnswerSetting answerSetting = new AnswerSetting();

	@Data
	public class AnswerSetting {

		/** 需要密码填写 */
		private String password;

		/**
		 * 显示填写进度条
		 */
		private Boolean progressBar = true;

		/**
		 * 是否显示题号
		 */
		private Boolean questionNumber = true;

		/** 自动保存结果 */
		private Boolean autoSave;

		/** 允许更新答案 */
		private Boolean enableUpdate;

		private LinkedHashMap initialValues;
	}

}
