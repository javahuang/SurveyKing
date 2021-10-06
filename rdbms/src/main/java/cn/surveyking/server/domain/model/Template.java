package cn.surveyking.server.domain.model;

import cn.surveyking.server.domain.dto.SurveySchemaType;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * 问题模板表
 *
 * @author javahuang
 * @date 2021/9/23
 */
@Data
@TableName(value = "t_template", autoResultMap = true)
@Accessors(chain = true)
public class Template {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 模板标题
	 */
	private String name;

	/**
	 * 问题类型
	 */
	private SurveySchemaType.QuestionType questionType;

	/**
	 * 问题模板
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private SurveySchemaType template;

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

	@TableField(fill = FieldFill.INSERT, select = false)
	private Date createAt;

	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private Date updateAt;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private String updateBy;

	/**
	 * 与其他用户共享
	 */
	private Integer shared;

	@TableLogic
	private Integer deleted;

}
