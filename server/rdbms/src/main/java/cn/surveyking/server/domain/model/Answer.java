package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import cn.surveyking.server.domain.dto.AnswerExamInfo;
import cn.surveyking.server.domain.dto.AnswerMetaInfo;
import cn.surveyking.server.domain.dto.SurveySchema;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
@TableName(value = "t_answer", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Answer extends BaseModel {

	private String projectId;

	/**
	 * 暂存的答案
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private LinkedHashMap tempAnswer;

	/**
	 * 最终答案
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private LinkedHashMap answer;

	/**
	 * 问卷
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private SurveySchema survey;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private AnswerMetaInfo metaInfo;

	/**
	 * 考试分数
	 */
	private Double examScore;

	/**
	 * 考试信息
	 */
	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private AnswerExamInfo examInfo;

	/**
	 * 0 暂存 1 已完成
	 */
	private Integer tempSave;

	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE)
	private Date updateAt;

}
