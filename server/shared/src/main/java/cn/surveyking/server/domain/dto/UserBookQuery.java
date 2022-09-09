package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author javahuang
 * @date 2022/09/08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserBookQuery extends PageQuery {

	private Integer type;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	private String name;

}
