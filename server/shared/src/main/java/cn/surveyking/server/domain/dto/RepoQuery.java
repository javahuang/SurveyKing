package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RepoQuery extends PageQuery {

	private String id;

	private String password;

	private String name;

}
