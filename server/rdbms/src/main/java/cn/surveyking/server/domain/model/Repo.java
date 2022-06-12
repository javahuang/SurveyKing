package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

/**
 * 模板组
 *
 * @TableName t_repo
 */
@Data
@TableName(value = "t_repo", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Repo extends BaseModel {

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

	@JsonIgnore
	@TableField(exist = false)
	private boolean deleted = false;

}