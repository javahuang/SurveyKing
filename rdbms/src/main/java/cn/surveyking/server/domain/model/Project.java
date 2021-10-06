package cn.surveyking.server.domain.model;

import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.SurveySchemaType;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
@TableName(value = "t_project", autoResultMap = true)
@Accessors(chain = true)
public class Project {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String shortId;

	private String name;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private SurveySchemaType survey;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGNVARCHAR)
	private ProjectSetting setting;

	// 1已发布 0未发布
	private Integer status;

	@TableField(fill = FieldFill.INSERT, select = false)
	private Date createAt;

	@TableField(fill = FieldFill.INSERT, select = false)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private Date updateAt;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private String updateBy;

	private String belongGroup;

	@TableLogic
	private Integer deleted;

}
