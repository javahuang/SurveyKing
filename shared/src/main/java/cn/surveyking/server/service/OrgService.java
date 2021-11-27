package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.OrgRequest;
import cn.surveyking.server.domain.dto.OrgSortRequest;
import cn.surveyking.server.domain.dto.OrgView;
import cn.surveyking.server.workflow.domain.dto.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/11/2
 */
public interface OrgService {

	/** 查询所有的组织机构信息 */
	List<OrgView> listOrg();

	void addOrg(OrgRequest request);

	void updateOrg(OrgRequest request);

	void deleteOrg(String id);

	void sortOrg(OrgSortRequest request);
}
