package cn.surveyking.server.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_comm_dict
 */
@TableName(value = "t_comm_dict")
@Data
public class CommDict implements Serializable {

	/**
	 *
	 */
	@TableId(value = "id")
	private String id;

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

	/**
	 * 创建时间
	 */
	@TableField(value = "create_at")
	private Date createAt;

	/**
	 *
	 */
	@TableField(value = "create_by")
	private String createBy;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_at")
	private Date updateAt;

	/**
	 *
	 */
	@TableField(value = "update_by")
	private String updateBy;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

}