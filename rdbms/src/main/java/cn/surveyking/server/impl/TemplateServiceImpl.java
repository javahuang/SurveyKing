package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.workflow.domain.dto.*;
import cn.surveyking.server.domain.mapper.TemplateViewMapper;
import cn.surveyking.server.domain.model.Template;
import cn.surveyking.server.mapper.TemplateMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.TemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TemplateServiceImpl extends BaseService<TemplateMapper, Template> implements TemplateService {

	private final TemplateViewMapper templateViewMapper;

	@Override
	public PaginationResponse<TemplateView> listTemplate(TemplateQuery query) {
		Page<Template> templatePage = pageByQuery(query, Wrappers.<Template>lambdaQuery()
				.like(isNotEmpty(query.getName()), Template::getName, query.getName())
				.eq(query.getQuestionType() != null, Template::getQuestionType, query.getQuestionType())
				// 默认查询额是普通题型
				.ne(query.getQuestionType() == null, Template::getQuestionType, SurveySchemaType.QuestionType.Survey)
				.in(query.getCategories().size() > 0, Template::getCategory, query.getCategories())
				.eq(Template::getShared, query.getShared())
				.eq(query.getShared() == 0, Template::getCreateBy, SecurityContextUtils.getUserId())
				.and(query.getTags().size() > 0, i -> query.getTags().forEach(tag -> {
					i.or(j -> j.like(Template::getTag, tag));
				})).orderByAsc(Template::getPriority));
		return new PaginationResponse<>(templatePage.getTotal(),
				templatePage.getRecords().stream().map(x -> templateViewMapper.toView(x)).collect(Collectors.toList()));
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
	public List<String> listTemplateCategories(CategoryQuery query) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DISTINCT category");
		queryWrapper.like(hasText(query.getName()), "category", query.getName());
		queryWrapper.eq("shared", query.getShared());
		queryWrapper.eq("question_type", query.getQuestionType());
		return list(queryWrapper).stream().filter(x -> x != null).map(x -> x.getCategory())
				.collect(Collectors.toList());
	}

	@Override
	public Set<String> listTemplateTags(TagQuery query) {
		QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DISTINCT tag").like(hasText(query.getName()), "tag", query.getName()).isNotNull("tag");
		queryWrapper.eq("shared", query.getShared());
		queryWrapper.eq("question_type", query.getQuestionType());
		Set<String> tags = new HashSet<>();
		list(queryWrapper).stream().filter(x -> x != null).forEach(x -> {
			tags.addAll(Arrays.asList(x.getTag()));
		});
		return tags;
	}

}
