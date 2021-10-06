package cn.surveyking.server.core.pagination;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Data
public class PaginationResponse<T> {

	private Long total;

	private List<T> data;

	private Integer current;

	private Integer pageSize;

	public PaginationResponse() {
	}

	public PaginationResponse(Long total, List<T> data) {
		this.total = total;
		this.data = data;
	}

}
