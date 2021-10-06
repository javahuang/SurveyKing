package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/8/31
 */
@Data
public class PageQuery {

	private int current = 1;

	private int pageSize = 20;

	public int getPageSize() {
		if (this.pageSize == -1) {
			return Integer.MAX_VALUE;
		}
		return pageSize;
	}

}
