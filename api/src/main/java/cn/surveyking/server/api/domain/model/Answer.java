package cn.surveyking.server.api.domain.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
@TableName(value = "t_answer", autoResultMap = true)
@Accessors(chain = true)
public class Answer {

	private String id;

	private String shortId;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private LinkedHashMap answer;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private AnswerMetaInfo metaInfo = new AnswerMetaInfo();

	/**
	 * 0 暂存 1 已完成
	 */
	private Integer tempSave;

	@TableField(fill = FieldFill.INSERT, select = false)
	private Date createAt;

	@TableField(fill = FieldFill.INSERT, select = false)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private Date updateAt;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private String updateBy;

	@TableLogic
	private Integer deleted;

}
