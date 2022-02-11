package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/1
 */
@Data
public class SelectDeptRequest {

	private String name;

	private String projectId;

	private List<String> selected = new ArrayList<>();

}
