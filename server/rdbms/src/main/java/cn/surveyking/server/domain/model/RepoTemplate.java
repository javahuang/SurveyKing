package cn.surveyking.server.domain.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板库模板
 *
 * @TableName t_repo_template
 */
@Data
@TableName(value = "t_repo_template", autoResultMap = true)
public class RepoTemplate implements Serializable {

	/**
	 *
	 */
	private String id;

	/**
	 * 模板id
	 */
	private String templateId;

	/**
	 * 模板库id
	 */
	private String repoId;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT, select = false)
	private Date createAt;

	/**
	 *
	 */
	@TableField(fill = FieldFill.INSERT, select = false)
	private String createBy;

	private static final long serialVersionUID = 1L;

}