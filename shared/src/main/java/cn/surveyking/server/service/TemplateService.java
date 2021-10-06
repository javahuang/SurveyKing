package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.TemplateQuery;
import cn.surveyking.server.domain.dto.TemplateRequest;
import cn.surveyking.server.domain.dto.TemplateView;

import java.util.List;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/9/23
 */
public interface TemplateService {

	List<TemplateView> listTemplate(TemplateQuery query);

	String addTemplate(TemplateRequest template);

	void updateTemplate(TemplateRequest template);

	void deleteTemplate(String id);

	List<String> listTemplateCategories(String search);

	Set<String> listTemplateTags(String search);

}
