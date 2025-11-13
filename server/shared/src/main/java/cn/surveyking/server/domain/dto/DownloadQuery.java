package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownloadQuery extends PageQuery {

	/**
	 * 问卷 id
	 */
	@NotNull
	private String projectId;

	/**
	 * 当前答案id
	 */
	private String answerId;

	/**
	 * 导出附件命名表达式
	 */
	private String nameExp;

	/**
	 * 答案ids
	 */
	private List<String> ids;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	@NotNull
	private DownloadType type;

	/**
	 * 浏览器语言
	 */
	private String locale;

	public enum DownloadType {

		/**
		 * 问卷答案、问卷答案+附件、问卷附件
		 */
		answer, answerAttachment, attachment

	}

}
