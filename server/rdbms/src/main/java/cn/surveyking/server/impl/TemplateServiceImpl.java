package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.TemplateViewMapper;
import cn.surveyking.server.domain.model.Tag;
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

	private final TagServiceImpl tagService;

	@Override
	public PaginationResponse<TemplateView> listTemplate(TemplateQuery query) {
		Page<Template> templatePage = pageByQuery(query, Wrappers.<Template>lambdaQuery()
				.like(isNotEmpty(query.getName()), Template::getName, query.getName())
				.eq(query.getQuestionType() != null, Template::getQuestionType, query.getQuestionType())
				// 默认查询额是普通题型
				// .ne(query.getQuestionType() != null, Template::getQuestionType,
				// query.getQuestionType())
				.in(query.getCategories().size() > 0, Template::getCategory, query.getCategories())
				.eq(query.getMode() != null, Template::getMode, query.getMode())
				.exists(query.getRepoId() != null, String.format(
						"select 1 from t_repo_template t where t.repo_id = '%s' and t.template_id = t_template.id",
						query.getRepoId()))
				.exists(query.getTag().size() > 0,
						String.format("select 1 from t_tag t where t.entity_id = t_template.id and t.name in (%s)",
								query.getTag().stream().map(x -> "'" + x + "'").collect(Collectors.joining(","))))
				.eq(query.getShared() != null, Template::getShared, query.getShared())
				.eq(query.getShared() != null && query.getShared() == 0, Template::getCreateBy,
						SecurityContextUtils.getUserId())
				.orderByAsc(Template::getPriority));
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
	public void batchAddTemplate(List<TemplateRequest> templateRequests) {
		saveBatch(templateViewMapper.fromRequest(templateRequests));
	}

	@Override
	public void batchUpdateTemplate(List<TemplateRequest> templateRequests) {
		updateBatchById(templateViewMapper.fromRequest(templateRequests));
	}

	@Override
	public void updateTemplate(TemplateRequest request) {
		updateById(templateViewMapper.fromRequest(request));
	}

	@Override
	public void deleteTemplate(List<String> ids) {
		removeBatchByIds(ids);
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
	public Set<String> getTags(TagQuery query) {
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DISTINCT name").like(hasText(query.getName()), "name", query.getName())
				.eq("category", query.getCategory()).last("limit 20");
		Set<String> tags = new HashSet<>();
		tagService.list(queryWrapper).stream().filter(x -> x != null).forEach(x -> {
			tags.addAll(Arrays.asList(x.getName()));
		});
		return tags;
	}

}
