package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import cn.surveyking.server.domain.dto.SurveySchema;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

/**
 * 问题模板表
 *
 * @author javahuang
 * @date 2021/9/23
 */
@Data
@TableName(value = "t_template", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Template extends BaseModel {

	/**
	 * 模板标题
	 */
	private String name;

	/**
	 * 问题类型
	 */
	private SurveySchema.QuestionType questionType;

	/**
	 * 问题模板
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private SurveySchema template;

	/**
	 * 标签
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private String[] tag;

	/**
	 * 模板分类
	 */
	private String category;

	/**
	 * 预览地址
	 */
	private String previewUrl;

	/**
	 * 排序优先级，值越小优先级越高
	 */
	private Integer priority;

	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 与其他用户共享
	 */
	private Integer shared;

}
