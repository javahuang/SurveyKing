package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/4/24
 */
@Data
public class UserOverview {

	/**
	 * 考试数量
	 */
	private Long examCount;

	/**
	 * 问卷数量
	 */
	private Long surveyCount;

}
