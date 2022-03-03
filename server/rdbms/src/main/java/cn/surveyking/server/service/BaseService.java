package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.PageQuery;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author javahuang
 * @date 2021/10/12
 */
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

	public <E extends IPage<T>> Page<T> pageByQuery(PageQuery pageQuery) {
		Page<T> page = new Page<T>(pageQuery.getCurrent(), pageQuery.getPageSize());
		return super.page(page);
	}

	public <E extends IPage<T>> Page pageByQuery(PageQuery pageQuery, Wrapper<T> queryWrapper) {
		Page<T> page = new Page<T>(pageQuery.getCurrent(), pageQuery.getPageSize());
		return super.page(page, queryWrapper);
	}

}
