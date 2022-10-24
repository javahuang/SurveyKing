package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_comm_dict
 */
@TableName(value = "t_comm_dict")
@Data
@EqualsAndHashCode(callSuper = false)
public class CommDict extends BaseModel {

	/**
	 * 字典编码
	 */
	@TableField(value = "code")
	private String code;

	/**
	 * 字典中文名称
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 备注信息
	 */
	@TableField(value = "remark")
	private String remark;

	/**
	 * 字典类型 1:问卷字典
	 */
	@TableField(value = "dict_type")
	private Integer dictType;

	@TableField(exist = false)
	private Boolean deleted = false;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

}