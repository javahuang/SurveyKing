package cn.surveyking.server.domain.model;

import cn.surveyking.server.domain.dto.DashboardSetting;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 仪表盘
 *
 * @TableName t_dashboard
 */
@TableName(value = "t_dashboard")
@Data
public class Dashboard implements Serializable {

	/**
	 * ID
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 仪表盘组件key
	 */
	@TableField(value = "key")
	private String key;

	/**
	 * 仪表盘分类
	 */
	@TableField(value = "category")
	private Integer type;

	/**
	 * 项目ID
	 */
	@TableField(value = "project_id")
	private String projectId;

	/**
	 * 仪表盘设置
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, value = "setting")
	private DashboardSetting setting;

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