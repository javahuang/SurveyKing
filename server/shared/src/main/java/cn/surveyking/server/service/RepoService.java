package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;

import java.util.List;

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
	 * 
	 * @param id
	 * @return
	 */
	RepoView getRpo(String id);

	/**
	 * 添加题库
	 * 
	 * @param request 题库
	 */
	void addRepo(RepoRequest request);

	/**
	 * 更新题库
	 * 
	 * @param request 题库
	 */
	void updateRepo(RepoRequest request);

	/**
	 * 删除题库
	 * 
	 * @param request 题库
	 */
	void deleteRepo(RepoRequest request);

	/**
	 * 批量添加题库模板
	 * 
	 * @param request
	 */
	void batchAddRepoTemplate(RepoTemplateRequest request);

	/**
	 * 解除题库与模板的绑定关系
	 * 
	 * @param request
	 */
	void batchUnBindTemplate(RepoTemplateRequest request);

	/**
	 * 从题库里面挑选试题
	 * 
	 * @param repos
	 * @return
	 */
	List<SurveySchema> pickQuestionFromRepo(List<ProjectSetting.RandomSurveyCondition> repos);

	void importFromTemplate(RepoTemplateRequest request);

	PaginationResponse<UserBookView> listUserBook(UserBookQuery query);

	void createUserBook(UserBookRequest request);

	UserBookView updateUserBook(UserBookRequest request);

	void deleteUserBook(UserBookRequest request);

	List<RepoView> selectRepo(SelectRepoRequest request);

	/**
	 * 导出题库题目
	 * 
	 * @param request
	 */
	void exportRepoQuestions(RepoRequest request);
}
