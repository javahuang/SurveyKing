package cn.surveyking.server.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板组
 *
 * @TableName t_tag
 */
@Data
@TableName(value = "t_tag", autoResultMap = true)
public class Tag implements Serializable {

	/**
	 *
	 */
	private String id;

	/**
	 * 实体ID
	 */
	private String entityId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 分类
	 */
	private String category;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 *
	 */
	private String createBy;

	private static final long serialVersionUID = 1L;

}