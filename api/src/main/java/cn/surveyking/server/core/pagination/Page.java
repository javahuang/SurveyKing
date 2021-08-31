package cn.surveyking.server.core.pagination;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 重写原始 Page 类，目前是不序列化一些字段
 *
 * @author javahuang
 * @date 2021/8/6
 */
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

	public Page() {
	}

	public Page(long current, long size) {
		super(current, size);
	}

	public Page(long current, long size, long total) {
		super(current, size, total);
	}

	public Page(long current, long size, boolean searchCount) {
		super(current, size, searchCount);
	}

	public Page(long current, long size, long total, boolean searchCount) {
		super(current, size, total, searchCount);
	}

	@Override
	@JsonIgnore
	public boolean isOptimizeCountSql() {
		return this.optimizeCountSql;
	}

	@JsonIgnore
	public boolean isSearchCount() {
		return this.searchCount;
	}

	@Deprecated
	@JsonIgnore
	public List<OrderItem> getOrders() {
		return this.orders;
	}

}
