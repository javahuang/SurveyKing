package cn.surveyking.server.web.domain.model;

import cn.surveyking.server.web.domain.handler.AttachmentListTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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

	@TableField(typeHandler = AttachmentListTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private List<Attachment> attachment;

	@TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.LONGVARCHAR)
	private AnswerMetaInfo metaInfo;

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

	@Data
	public static class Attachment {

		/**
		 * 附件id
		 */
		private String id;

		/**
		 * 附件原始名字
		 */
		private String originalName;

		/**
		 * 内容类型
		 */
		private String contentType;

	}

}
