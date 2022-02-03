package cn.surveyking.server.service;

import cn.surveyking.server.core.constant.CacheConsts;
import cn.surveyking.server.domain.dto.DeptRequest;
import cn.surveyking.server.domain.dto.DeptSortRequest;
import cn.surveyking.server.domain.dto.DeptView;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/11/2
 */
public interface DeptService {

	/** 查询所有的组织机构信息 */
	@Cacheable(cacheNames = CacheConsts.deptCacheName, key = "'all'")
	List<DeptView> listDept();

	DeptView getDept(String id);

	@CacheEvict(cacheNames = CacheConsts.deptCacheName, allEntries = true)
	void addDept(DeptRequest request);

	@CacheEvict(cacheNames = CacheConsts.deptCacheName, allEntries = true)
	void updateDept(DeptRequest request);

	@CacheEvict(cacheNames = CacheConsts.deptCacheName, allEntries = true)
	void deleteDept(String id);

	@CacheEvict(cacheNames = CacheConsts.deptCacheName, allEntries = true)
	void sortDept(DeptSortRequest request);

}
