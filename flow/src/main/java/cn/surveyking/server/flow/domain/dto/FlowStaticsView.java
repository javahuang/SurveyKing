package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/2/16
 */
@Data
public class FlowStaticsView {

	/**
	 * 待办
	 */
	long todo;

	/**
	 * 已完成
	 */
	long finished;

	/**
	 * 抄送给我的
	 */
	long copyTo;

	/**
	 * 我发起的
	 */
	long selfCreated;

}
