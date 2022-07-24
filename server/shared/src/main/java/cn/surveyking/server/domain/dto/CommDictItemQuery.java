package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommDictItemQuery extends PageQuery {

	private String dictCode;

}
