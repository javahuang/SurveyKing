package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class OrgRequest {

	private String id;

	private String parentId;

	private String name;

	private String shortName;

	private String code;

	/** 负责人id */
	private String managerId;

	private String remark;

	/** 拓展属性 */
	private String propertyJson;

}
