package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.model.BaseModel;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.SurveySchema;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
@TableName(value = "t_project", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Project extends BaseModel {

	private String name;

	/**
	 * 问卷模式 survey问卷 exam考试
	 */
	private ProjectModeEnum mode;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private SurveySchema survey;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGNVARCHAR)
	private ProjectSetting setting;

	// 1已发布 0未发布
	private Integer status;

	private String belongGroup;

	@TableField(fill = FieldFill.INSERT)
	private Date createAt;

	@TableField(fill = FieldFill.UPDATE)
	private Date updateAt;

}
