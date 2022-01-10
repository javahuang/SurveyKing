package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.OrgRequest;
import cn.surveyking.server.domain.dto.OrgSortRequest;
import cn.surveyking.server.domain.dto.OrgView;
import cn.surveyking.server.domain.mapper.OrgDtoMapper;
import cn.surveyking.server.domain.model.Org;
import cn.surveyking.server.domain.model.UserPosition;
import cn.surveyking.server.mapper.OrgMapper;
import cn.surveyking.server.mapper.UserPositionMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.OrgService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class OrgServiceImpl extends BaseService<OrgMapper, Org> implements OrgService {

	private final OrgDtoMapper orgDtoMapper;

	private final UserService userService;

	private final UserPositionMapper userPositionMapper;

	@Override
	public List<OrgView> listOrg() {
		List<OrgView> result = orgDtoMapper.toView(list(Wrappers.<Org>lambdaQuery().orderByAsc(Org::getSortCode)));
		result.forEach(orgView -> {
			String managerId = orgView.getManagerId();
			if (isNotBlank(managerId)) {
				orgView.setManagerName(userService.loadUserById(managerId).getName());
			}
		});
		return result;
	}

	@Override
	public void addOrg(OrgRequest request) {
		Org org = orgDtoMapper.fromRequest(request);
		org.setSortCode((int) count(Wrappers.<Org>lambdaQuery().eq(Org::getParentId, request.getParentId())));
		save(org);
	}

	@Override
	public void updateOrg(OrgRequest request) {
		updateById(orgDtoMapper.fromRequest(request));
	}

	@Override
	public void deleteOrg(String id) {
		removeById(id);
		userPositionMapper.delete(Wrappers.<UserPosition>lambdaQuery().eq(UserPosition::getOrgId, id));
	}

	@Override
	public void sortOrg(OrgSortRequest request) {
		for (int i = 0; i < request.getNodes().size(); i++) {
			Org org = getById(request.getNodes().get(i));
			org.setSortCode(i);
			updateById(org);
		}
	}

}
