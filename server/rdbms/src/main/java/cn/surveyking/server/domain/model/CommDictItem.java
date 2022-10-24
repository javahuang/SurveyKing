package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_comm_dict_item
 */
@TableName(value = "t_comm_dict_item")
@Data
public class CommDictItem extends BaseModel {

	/**
	 * 字典编码
	 */
	@TableField(value = "dict_code")
	private String dictCode;

	/**
	 * 字典项中文名称
	 */
	@TableField(value = "item_name")
	private String itemName;

	/**
	 * 字典项值
	 */
	@TableField(value = "item_value")
	private String itemValue;

	/**
	 * 父字典项值
	 */
	@TableField(value = "parent_item_value")
	private String parentItemValue;

	/**
	 * 层级
	 */
	@TableField(value = "item_level")
	private Integer itemLevel;

	/**
	 * 字典项顺序
	 */
	@TableField(value = "item_order")
	private Integer itemOrder;

	@TableField(exist = false)
	private Boolean deleted = false;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

}