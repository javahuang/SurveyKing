package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Data
public class CommDictItemView {

	/**
	 *
	 */
	private String id;

	/**
	 * 字典编码
	 */
	private String dictCode;

	/**
	 * 字典项中文名称
	 */
	private String itemName;

	/**
	 * 字典项值
	 */
	private String itemValue;

	/**
	 * 父字典项值
	 */
	private String parentItemValue;

	/**
	 * 层级
	 */
	private Integer itemLevel;

	/**
	 * 字典项顺序
	 */
	private Integer itemOrder;

	private Date createAt;

}
