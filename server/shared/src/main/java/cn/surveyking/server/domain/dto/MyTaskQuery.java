package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 首页任务查询
 *
 * @author javahuang
 * @date 2022/7/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MyTaskQuery extends PageQuery {

	private String type;

}
