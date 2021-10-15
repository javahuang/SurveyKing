package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
public class RoleView {

	private String id;

	private String name;

	private String code;

	private String remark;

	private List<String> authorities;

	private Date createAt;

}
