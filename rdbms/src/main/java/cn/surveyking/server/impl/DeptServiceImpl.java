package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.DeptRequest;
import cn.surveyking.server.domain.dto.DeptView;
import cn.surveyking.server.domain.dto.DeptSortRequest;
import cn.surveyking.server.domain.dto.SelectDeptRequest;
import cn.surveyking.server.domain.mapper.DeptDtoMapper;
import cn.surveyking.server.domain.model.Dept;
import cn.surveyking.server.domain.model.UserPosition;
import cn.surveyking.server.mapper.DeptMapper;
import cn.surveyking.server.mapper.UserPositionMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.DeptService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DeptServiceImpl extends BaseService<DeptMapper, Dept> implements DeptService {

	private final DeptDtoMapper deptDtoMapper;

	private final UserService userService;

	private final UserPositionMapper userPositionMapper;

	@Override
	public List<DeptView> listDept(SelectDeptRequest request) {
		if (request == null) {
			request = new SelectDeptRequest();
		}
		List<DeptView> result = deptDtoMapper.toView(list(Wrappers.<Dept>lambdaQuery()
				.in(!CollectionUtils.isEmpty(request.getSelected()), Dept::getId, request.getSelected())
				.orderByAsc(Dept::getSortCode)));
		result.forEach(orgView -> {
			String managerId = orgView.getManagerId();
			if (isNotBlank(managerId)) {
				orgView.setManagerName(userService.loadUserById(managerId).getName());
			}
		});
		return result;
	}

	@Override
	public DeptView getDept(String id) {
		return deptDtoMapper.toView(getById(id));
	}

	@Override
	public void addDept(DeptRequest request) {
		Dept dept = deptDtoMapper.fromRequest(request);
		if (StringUtils.isEmpty(request.getParentId())) {
			dept.setParentId("0");
		}
		dept.setSortCode((int) count(Wrappers.<Dept>lambdaQuery().eq(
				Dept::getParentId, request.getParentId())));
		save(dept);
	}

	@Override
	public void updateDept(DeptRequest request) {
		updateById(deptDtoMapper.fromRequest(request));
	}

	@Override
	public void deleteDept(String id) {
		removeById(id);
		userPositionMapper.delete(Wrappers.<UserPosition>lambdaQuery().eq(UserPosition::getDeptId, id));
	}

	@Override
	public void sortDept(DeptSortRequest request) {
		for (int i = 0; i < request.getNodes().size(); i++) {
			Dept dept = getById(request.getNodes().get(i));
			dept.setSortCode(i);
			updateById(dept);
		}
	}

}
