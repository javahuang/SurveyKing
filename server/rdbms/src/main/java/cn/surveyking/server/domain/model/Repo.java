package cn.surveyking.server.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板组
 *
 * @TableName t_repo
 */
@Data
@TableName(value = "t_repo", autoResultMap = true)
public class Repo implements Serializable {

	/**
	 *
	 */
	private String id;

	/**
	 * 标题
	 */
	private String name;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 标签
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private String[] tag;

	/**
	 * 排序优先级
	 */
	private Integer priority;

	/**
	 * 设置
	 */
	private String setting;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 *
	 */
	private String createBy;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	/**
	 *
	 */
	private String updateBy;

	private static final long serialVersionUID = 1L;

}