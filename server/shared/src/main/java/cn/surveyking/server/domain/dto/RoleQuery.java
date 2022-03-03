package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleQuery extends PageQuery {

	private String name;

}
