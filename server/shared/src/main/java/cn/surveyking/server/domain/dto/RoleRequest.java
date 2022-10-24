package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
public class RoleRequest {

	private String id;

	private String name;

	private String code;

	private String remark;

	/** 权限编码列表 */
	private List<String> authorities;

	private List<String> userIds;

	private List<String> evictUserIds;

	private Integer status;

}
