package cn.surveyking.server.web.service.impl;

import cn.surveyking.server.web.domain.dto.SurveySchemaType;
import cn.surveyking.server.web.domain.dto.TemplateQuery;
import cn.surveyking.server.web.domain.dto.TemplateView;
import cn.surveyking.server.web.domain.mapper.TemplateViewMapper;
import cn.surveyking.server.web.domain.model.Template;
import cn.surveyking.server.web.mapper.TemplateMapper;
import cn.surveyking.server.web.service.TemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

	private final TemplateMapper templateMapper;

	@Override
	public List<TemplateView> listTemplate(TemplateQuery query) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		if (query.getQuestionType() != null) {
			queryWrapper.eq("question_type", query.getQuestionType().toString());
		}
		// 默认情况下查问题模板
		queryWrapper.ne(query.getQuestionType() == null, "question_type", SurveySchemaType.QuestionType.Survey);
		queryWrapper.orderByAsc("priority");
		return TemplateViewMapper.INSTANCE.toViewList(templateMapper.selectList(queryWrapper));
	}

	@Override
	public String addTemplate(Template template) {
		templateMapper.insert(template);
		return template.getId();
	}

	@Override
	public void updateTemplate(Template template) {
		templateMapper.updateById(template);
	}

	@Override
	public void deleteTemplate(String id) {
		templateMapper.deleteById(id);
	}

	@Override
	public List<String> listTemplateCategories(String search) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DISTINCT category");
		queryWrapper.like(hasText(search), "category", search);
		return templateMapper.selectList(queryWrapper).stream().filter(x -> x != null).map(x -> x.getCategory())
				.collect(Collectors.toList());
	}

	@Override
	public Set<String> listTemplateTags(String search) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("tag");
		queryWrapper.like(hasText(search), "tag", search);
		Set<String> tags = new HashSet<>();
		templateMapper.selectList(queryWrapper).stream().filter(x -> x != null).forEach(x -> {
			tags.addAll(Arrays.asList(x.getTag()));
		});
		return tags;
	}

}
