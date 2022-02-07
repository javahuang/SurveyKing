package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/3
 */
@Data
public class SelectPositionRequest {

	private List<String> selected = new ArrayList<>();

}
