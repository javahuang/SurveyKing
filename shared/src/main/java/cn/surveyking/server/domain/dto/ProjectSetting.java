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
		 * 是否需要登录才能作答。 需要登录答卷的几种场景：1、问卷设置直接设置 2、工作流起始节点指定发起人 3、问卷里面有成员和部门题
		 */
		private Boolean loginRequired;

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
