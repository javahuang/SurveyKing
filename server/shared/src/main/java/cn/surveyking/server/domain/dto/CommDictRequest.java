package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Data
public class CommDictRequest {

	private String id;

	/**
	 * 字典编码
	 */
	private String code;

	/**
	 * 字典中文名称
	 */
	private String name;

	/**
	 * 备注信息
	 */
	private String remark;

	/**
	 * 字典类型 1:问卷字典
	 */
	private Integer dictType;

}
