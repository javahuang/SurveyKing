package cn.surveyking.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SelectUserRequest extends PageQuery {

	private String name;

	private String projectId;

	private String deptId;

	private List<String> selected = new ArrayList<>();

	public String getName() {
		if (name != null) {
			return "%" + name + "%";
		}
		return null;
	}

}
