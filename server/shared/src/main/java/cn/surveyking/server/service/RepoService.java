package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.RepoQuery;
import cn.surveyking.server.domain.dto.RepoRequest;
import cn.surveyking.server.domain.dto.RepoTemplateRequest;
import cn.surveyking.server.domain.dto.RepoView;

/**
 * @author javahuang
 * @date 2022/4/27
 */
public interface RepoService {

	/**
	 * @param query
	 * @return 题库列表
	 */
	PaginationResponse<RepoView> listRepo(RepoQuery query);

	/**
	 * 获取单个模板库
	 * @param id
	 * @return
	 */
	RepoView getRpo(String id);

	/**
	 * 添加题库
	 * @param request 题库
	 */
	void addRepo(RepoRequest request);

	/**
	 * 更新题库
	 * @param request 题库
	 */
	void updateRepo(RepoRequest request);

	/**
	 * 删除题库
	 * @param id 题库
	 */
	void deleteRepo(String id);

	/**
	 * 批量添加题库模板
	 * @param request
	 */
	void batchAddRepoTemplate(RepoTemplateRequest request);

	/**
	 * 解除题库与模板的绑定关系
	 * @param request
	 */
	void batchUnBindTemplate(RepoTemplateRequest request);

}
