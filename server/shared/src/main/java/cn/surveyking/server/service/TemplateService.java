package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;

import java.util.List;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/9/23
 */
public interface TemplateService {

	PaginationResponse<TemplateView> listTemplate(TemplateQuery query);

	String addTemplate(TemplateRequest template);

	void batchAddTemplate(List<TemplateRequest> templateRequests);

	void batchUpdateTemplate(List<TemplateRequest> templateRequests);

	void updateTemplate(TemplateRequest template);

	void deleteTemplate(List<String> ids);

	List<String> listTemplateCategories(CategoryQuery query);

	Set<String> getTags(TagQuery query);

}
