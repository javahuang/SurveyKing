package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Data
public class CommDictView {

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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createAt;

}
