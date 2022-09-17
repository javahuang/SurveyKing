package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import lombok.Data;

/**
 * @author javahuang
 * @date 2022/9/11
 */
@Data
public class SelectCategoryRequest {

	private ProjectModeEnum mode;

}
