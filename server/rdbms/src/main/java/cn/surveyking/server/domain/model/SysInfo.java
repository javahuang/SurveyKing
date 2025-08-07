package cn.surveyking.server.domain.model;

import cn.surveyking.server.domain.dto.SystemInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统信息
 *
 * @TableName t_sys_info
 */
@TableName(value = "t_sys_info", autoResultMap = true)
@Data
public class SysInfo implements Serializable {

	/**
	 * 主键
	 */
	@TableId(value = "id")
	private String id;

	/**
	 * 系统名称
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 系统描述信息
	 */
	@TableField(value = "description")
	private String description;

	/**
	 * 图标
	 */
	@TableField(value = "avatar")
	private String avatar;

	/**
	 * 默认语言
	 */
	@TableField(value = "locale")
	private String locale;

	/**
	 * 系统版本
	 */
	@TableField(value = "version")
	private String version;

	/**
	 * 是否默认设置
	 */
	@TableField(value = "is_default")
	private Boolean isDefault;

	/**
	 * 注册信息
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private SystemInfo.RegisterInfo registerInfo;

	/**
	 * 其他设置信息
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private SystemInfo.SystemSetting setting;

	/**
	 * AI设置
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private SystemInfo.AiSetting aiSetting;

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