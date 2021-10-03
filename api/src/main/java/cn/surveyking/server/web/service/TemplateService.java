package cn.surveyking.server.web.service;

import cn.surveyking.server.web.domain.dto.TemplateQuery;
import cn.surveyking.server.web.domain.dto.TemplateView;
import cn.surveyking.server.web.domain.model.Template;

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
