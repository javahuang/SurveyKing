package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Data
public class DeptView {

	private String id;

	private String parentId;

	private String name;

	private String shortName;

	private String code;

	/** 负责人id */
	private String managerId;

	/** 负责人名字 */
	private String managerName;

	private String remark;

	/** 拓展属性 */
	private String propertyJson;

}
