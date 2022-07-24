package cn.surveyking.server.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author javahuang
 * @date 2022/7/20
 */
@Data
public class PublicDictRequest {

	@NotBlank(message = "字典编码不能为空")
	private String dictCode;

	/**
	 * 查询的值
	 */
	private String search;

	/**
	 * 父字典项值
	 */
	private String parentValue;

	/**
	 * 最大查询记录数
	 */
	private Integer limit;

	/**
	 * 级联层级
	 */
	private Integer cascaderLevel;

}
