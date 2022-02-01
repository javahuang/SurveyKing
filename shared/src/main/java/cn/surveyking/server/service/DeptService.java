package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.DeptRequest;
import cn.surveyking.server.domain.dto.DeptSortRequest;
import cn.surveyking.server.domain.dto.DeptView;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/11/2
 */
public interface DeptService {

	/** 查询所有的组织机构信息 */
	List<DeptView> listOrg();

	void addDept(DeptRequest request);

	void updateDept(DeptRequest request);

	void deleteDept(String id);

	void sortDept(DeptSortRequest request);

}
