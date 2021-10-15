package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/8/31
 */
@Data
public class ProjectQuery extends PageQuery {

	private String shortId;

	private String password;

	private String name;

}
