package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.TagCategoryEnum;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.RepoViewMapper;
import cn.surveyking.server.domain.model.Repo;
import cn.surveyking.server.domain.model.RepoTemplate;
import cn.surveyking.server.domain.model.Tag;
import cn.surveyking.server.mapper.RepoMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.RepoService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class RepoServiceImpl extends BaseService<RepoMapper, Repo> implements RepoService {

	private final RepoTemplateServiceImpl repoTemplateService;

	private final TemplateServiceImpl templateService;

	private final RepoViewMapper repoViewMapper;

	private final TagServiceImpl tagService;

	@Override
	public PaginationResponse<RepoView> listRepo(RepoQuery query) {
		Page<Repo> page = pageByQuery(query, Wrappers.<Repo>lambdaQuery()
				.eq(isNotBlank(query.getName()), Repo::getName, query.getName()).orderByAsc(Repo::getCreateAt));
		PaginationResponse<RepoView> result = new PaginationResponse<>(page.getTotal(),
				repoViewMapper.toView(page.getRecords()));
		result.getList().forEach(repoView -> {
			repoView.setTotal(repoTemplateService
					.count(Wrappers.<RepoTemplate>lambdaQuery().eq(RepoTemplate::getRepoId, repoView.getId())));
		});
		return result;
	}

	@Override
	public RepoView getRpo(String id) {
		return repoViewMapper.toView(getById(id));
	}

	@Override
	public void addRepo(RepoRequest request) {
		request.setId(IdWorker.getIdStr());
		save(repoViewMapper.fromRequest(request));
		tagService.batchAddTag(request.getId(), TagCategoryEnum.repo, request.getTag());
	}

	@Override
	public void updateRepo(RepoRequest request) {
		updateById(repoViewMapper.fromRequest(request));
		tagService.batchAddTag(request.getId(), TagCategoryEnum.repo, request.getTag());
	}

	@Override
	public void deleteRepo(String id) {
		removeById(id);
		// 解除题库与问题的绑定关系
		repoTemplateService.remove(Wrappers.<RepoTemplate>lambdaUpdate().eq(RepoTemplate::getRepoId, id));
		// 取消题库标签
		tagService.remove(Wrappers.<Tag>lambdaUpdate().eq(Tag::getEntityId, id));
	}

	@Override
	public void batchAddRepoTemplate(RepoTemplateRequest request) {
		List<Tag> tagList = new ArrayList<>();
		List<TemplateRequest> templatesAdd = new ArrayList<>();
		List<TemplateRequest> templatesUpdate = new ArrayList<>();
		List<RepoTemplate> repoTemplatesAdd = new ArrayList<>();

		request.getTemplates().forEach(template -> {
			RepoTemplate repoTemplate = new RepoTemplate();
			if (template.getId() == null) {
				template.setId(IdWorker.getIdStr());
				templatesAdd.add(template);

				if (request.getRepoId() != null) {
					repoTemplate.setRepoId(request.getRepoId());
					repoTemplate.setTemplateId(template.getId());
					repoTemplatesAdd.add(repoTemplate);
				}
			}
			else {
				templatesUpdate.add(template);
			}
			// template 里面的 tags 冗余了
			if (template.getTemplate().getTags() != null) {
				template.setTag(template.getTemplate().getTags().toArray(new String[0]));
			}

			List<String> tags = template.getTemplate().getTags();
			if (tags != null && tags.size() > 0) {
				tags.forEach(tagStr -> {
					Tag tag = new Tag();
					tag.setName(tagStr);
					tag.setEntityId(template.getId());
					tag.setCategory(TagCategoryEnum.template.name());
					tagList.add(tag);
				});
			}
			template.setQuestionType(template.getTemplate().getType());
		});

		if (templatesAdd.size() > 0) {
			// 添加模板的时候，需要添加题库与模板的关联关系
			templateService.batchAddTemplate(templatesAdd);
			repoTemplateService.saveBatch(repoTemplatesAdd);
		}
		if (templatesUpdate.size() > 0) {
			templateService.batchUpdateTemplate(templatesUpdate);
			// 更新模板时需要删除之前的标签
			tagService.remove(Wrappers.<Tag>lambdaUpdate().eq(Tag::getEntityId,
					templatesUpdate.stream().map(x -> x.getId()).collect(Collectors.toList())));
		}

		// 添加模板问题标签
		if (tagList.size() > 0) {
			tagService.saveBatch(tagList);
		}
	}

	@Override
	public void batchUnBindTemplate(RepoTemplateRequest request) {
		// 只有在问题页面才能执行物理删除，在题库问题列表里面只能解除绑定关系
		if (request.getRepoId() == null) {
			// 表示是物理删除
			templateService.removeBatchByIds(request.getIds());
			// 删除题库和题的绑定关系
			repoTemplateService
					.remove(Wrappers.<RepoTemplate>lambdaUpdate().in(RepoTemplate::getTemplateId, request.getIds()));
		}
		else {
			// 解除绑定关系
			repoTemplateService
					.remove(Wrappers.<RepoTemplate>lambdaUpdate().eq(RepoTemplate::getRepoId, request.getRepoId())
							.in(RepoTemplate::getTemplateId, request.getIds()));
		}
	}

}
