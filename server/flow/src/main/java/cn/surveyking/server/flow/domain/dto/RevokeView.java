package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

/**
 * 前端回退节点
 *
 * @author javahuang
 * @date 2022/1/20
 */
@Data
public class RevokeView {

	/**
	 * 节点 id
	 */
	private String activityId;

	/**
	 * 节点名称
	 */
	private String activityName;

}
