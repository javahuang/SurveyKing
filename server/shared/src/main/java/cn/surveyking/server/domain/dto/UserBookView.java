package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author javahuang
 * @date 2022/9/9
 */
@Data
public class UserBookView {

	private String id;

	/**
	 * 模板ID
	 */
	private String templateId;

	/**
	 * 问题名称
	 */
	private String name;

	/**
	 * 错误次数
	 */
	private Integer wrongTimes;

	/**
	 * 笔记
	 */
	private String note;

	/**
	 * 1标记为简单
	 */
	private Integer status;

	/**
	 * 1我的错题 2我的收藏
	 */
	private Integer type;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createAt;

	private Date updateAt;

	private Double qscore;

}
