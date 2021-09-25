package cn.surveyking.server.api.service;

import cn.surveyking.server.api.domain.dto.TemplateQuery;
import cn.surveyking.server.api.domain.dto.TemplateView;
import cn.surveyking.server.api.domain.model.Template;

import java.util.List;
import java.util.Set;

/**
 * @author javahuang
 * @date 2021/9/23
 */
public interface TemplateService {

	List<TemplateView> listTemplate(TemplateQuery query);

	String addTemplate(Template template);

	void updateTemplate(Template template);

	void deleteTemplate(String id);

	List<String> listTemplateCategories(String search);

	Set<String> listTemplateTags(String search);

}
