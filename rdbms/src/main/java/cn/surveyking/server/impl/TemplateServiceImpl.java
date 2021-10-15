package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.SurveySchemaType;
import cn.surveyking.server.domain.dto.TemplateQuery;
import cn.surveyking.server.domain.dto.TemplateRequest;
import cn.surveyking.server.domain.dto.TemplateView;
import cn.surveyking.server.domain.mapper.TemplateViewMapper;
import cn.surveyking.server.domain.model.Template;
import cn.surveyking.server.mapper.TemplateMapper;
import cn.surveyking.server.service.TemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

	private final TemplateViewMapper templateViewMapper;

	@Override
	public List<TemplateView> listTemplate(TemplateQuery query) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		if (query.getQuestionType() != null) {
			queryWrapper.eq("question_type", query.getQuestionType().toString());
		}
		// 默认情况下查问题模板
		queryWrapper.ne(query.getQuestionType() == null, "question_type", SurveySchemaType.QuestionType.Survey);
		queryWrapper.orderByAsc("priority");
		return templateViewMapper.toViewList(list(queryWrapper));
	}

	@Override
	public String addTemplate(TemplateRequest request) {
		Template template = templateViewMapper.fromRequest(request);
		save(template);
		return template.getId();
	}

	@Override
	public void updateTemplate(TemplateRequest request) {
		updateById(templateViewMapper.fromRequest(request));
	}

	@Override
	public void deleteTemplate(String id) {
		removeById(id);
	}

	@Override
	public List<String> listTemplateCategories(String search) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DISTINCT category");
		queryWrapper.like(hasText(search), "category", search);
		return list(queryWrapper).stream().filter(x -> x != null).map(x -> x.getCategory())
				.collect(Collectors.toList());
	}

	@Override
	public Set<String> listTemplateTags(String search) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("tag");
		queryWrapper.like(hasText(search), "tag", search);
		Set<String> tags = new HashSet<>();
		list(queryWrapper).stream().filter(x -> x != null).forEach(x -> {
			tags.addAll(Arrays.asList(x.getTag()));
		});
		return tags;
	}

}
