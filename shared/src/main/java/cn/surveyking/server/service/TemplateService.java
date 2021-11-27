package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.workflow.domain.dto.*;

import java.util.List;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/9/23
 */
public interface TemplateService {

	PaginationResponse<TemplateView> listTemplate(TemplateQuery query);

	String addTemplate(TemplateRequest template);

	void updateTemplate(TemplateRequest template);

	void deleteTemplate(String id);

	List<String> listTemplateCategories(CategoryQuery query);

	Set<String> listTemplateTags(TagQuery query);

}
