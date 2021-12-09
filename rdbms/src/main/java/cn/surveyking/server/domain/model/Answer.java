package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import cn.surveyking.server.domain.dto.AnswerMetaInfo;
import cn.surveyking.server.domain.handler.AttachmentListTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Data
@TableName(value = "t_answer", autoResultMap = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Answer extends BaseModel {

	private String projectId;

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
