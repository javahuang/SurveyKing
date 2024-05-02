package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

/**
 * 错题本
 *
 * @TableName t_user_book
 */
@TableName(value = "t_user_book", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class UserBook extends BaseModel {

	/**
	 * 模板ID
	 */
	@TableField(value = "template_id")
	private String templateId;

	/**
	 * 问题名称
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 错误次数
	 */
	@TableField(value = "wrong_times")
	private Integer wrongTimes;

	/**
	 * 正确次数
	 */
	@TableField(value = "correct_times")
	private Integer correctTimes;

	/**
	 * 笔记
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private String note;

	/**
	 * 1标记为简单
	 */
	@TableField(value = "status")
	private Integer status;

	/**
	 * 1我的错题 2我的收藏
	 */
	@TableField(value = "type")
	private Integer type;

	private String repoId;

	/**
	 * 是否标记
	 */
	private Boolean isMarked;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private Boolean deleted = false;

}