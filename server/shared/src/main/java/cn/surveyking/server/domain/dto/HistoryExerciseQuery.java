package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @author javahuang
 * @date 2024/4/30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HistoryExerciseQuery extends PageQuery {

	private String projectId;

	private Integer tempSave;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date[] createTime;

}
