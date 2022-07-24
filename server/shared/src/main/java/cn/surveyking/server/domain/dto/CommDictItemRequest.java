package cn.surveyking.server.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Data
public class CommDictItemRequest {

	private MultipartFile file;

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
	private Integer level;

	/**
	 * 字典项顺序
	 */
	private Integer itemOrder;

}
