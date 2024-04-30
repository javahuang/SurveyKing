package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.TagCategoryEnum;
import cn.surveyking.server.core.uitls.RepoTemplateExcelParseHelper;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.RepoViewMapper;
import cn.surveyking.server.domain.mapper.UserBookViewMapper;
import cn.surveyking.server.domain.model.Repo;
import cn.surveyking.server.domain.model.Tag;
import cn.surveyking.server.domain.model.Template;
import cn.surveyking.server.domain.model.UserBook;
import cn.surveyking.server.mapper.RepoMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.RepoService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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

	private final TemplateServiceImpl templateService;

	private final RepoViewMapper repoViewMapper;

	private final TagServiceImpl tagService;

	private final UserBookServiceImpl userBookService;

	private final UserBookViewMapper userBookViewMapper;

	@Override
	public PaginationResponse<RepoView> listRepo(RepoQuery query) {
		Page<Repo> page = pageByQuery(query,
				Wrappers.<Repo>lambdaQuery().like(isNotBlank(query.getName()), Repo::getName, query.getName())
						.and(x -> x.eq(Repo::getCreateBy, SecurityContextUtils.getUserId())
								.or(y -> y.eq(Repo::getShared, true)))
						.eq(query.getMode() != null, Repo::getMode, query.getMode()).orderByAsc(Repo::getCreateAt));
		PaginationResponse<RepoView> result = new PaginationResponse<>(page.getTotal(),
				repoViewMapper.toView(page.getRecords()));
		result.getList().forEach(repoView -> {
			repoView.setTotal(
					templateService.count(Wrappers.<Template>lambdaQuery().eq(Template::getRepoId, repoView.getId())));
			// 获取每个标签对应的题的数量
			repoView.setTemplateTags(this.getBaseMapper().selectRepoTemplateTags(repoView.getId()));
			// 获取每个问题类型对应的题的数量
			repoView.setRepoQuestionTypes(this.getBaseMapper().selectRepoQuestionTypes(repoView.getId()));
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
	public void deleteRepo(RepoRequest request) {
		String id = request.getId();
		removeById(id);
		// 删除题库下面的所有题
		templateService.remove(Wrappers.<Template>lambdaUpdate().eq(Template::getRepoId, id));
		// 取消题库标签
		tagService.remove(Wrappers.<Tag>lambdaUpdate().eq(Tag::getEntityId, id));
	}

	@Override
	public void batchAddRepoTemplate(RepoTemplateRequest request) {
		List<Tag> tagList = new ArrayList<>();
		List<TemplateRequest> templatesAdd = new ArrayList<>();
		List<TemplateRequest> templatesUpdate = new ArrayList<>();
		List<Template> templateListOfCurrentRepo = templateService.list(Wrappers.<Template>lambdaQuery()
				.eq(Template::getRepoId, request.getRepoId()));

		request.getTemplates().forEach(template -> {
			//  根据序号更新更新题库
			templateListOfCurrentRepo.stream().filter(t -> StringUtils.hasText(t.getSerialNo()) &&
					t.getSerialNo().equals(template.getSerialNo())
					&& t.getQuestionType() == template.getQuestionType()).findFirst().ifPresent(t -> {
				template.setId(t.getId());
			});

			if (template.getId() == null) {
				template.setId(IdWorker.getIdStr());
				templatesAdd.add(template);
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

		if (!templatesAdd.isEmpty()) {
			// 添加模板的时候，需要添加题库与模板的关联关系
			templatesAdd.forEach(x -> x.setRepoId(request.getRepoId()));
			templateService.batchAddTemplate(templatesAdd);
		}
		if (!templatesUpdate.isEmpty()) {
			templatesUpdate.forEach(x -> x.setRepoId(request.getRepoId()));
			templateService.batchUpdateTemplate(templatesUpdate);
			// 更新模板时需要删除之前的标签
			tagService.remove(Wrappers.<Tag>lambdaUpdate().in(Tag::getEntityId,
					templatesUpdate.stream().map(x -> x.getId()).collect(Collectors.toList())));
		}

		// 添加模板问题标签
		if (!tagList.isEmpty()) {
			tagService.saveBatch(tagList);
		}
	}

	@Override
	public void batchUnBindTemplate(RepoTemplateRequest request) {
		if (request.getIds() != null) {
			templateService.removeBatchByIds(request.getIds());
		}
	}

	/**
	 * 从题库里面挑选题目
	 * @param repos
	 * @return
	 */
	@Override
	public List<SurveySchema> pickQuestionFromRepo(List<ProjectSetting.RandomSurveyCondition> repos) {
		List<Template> templates = new ArrayList<>();
		repos.forEach(repo -> {
			List<Template> repoTemplates = templateService.list(Wrappers.<Template>lambdaQuery()
					.eq(Template::getRepoId, repo.getRepoId())
					.in(!CollectionUtils.isEmpty(repo.getTypes()), Template::getQuestionType, repo.getTypes())
					.exists(!CollectionUtils.isEmpty(repo.getTags()),
							String.format("select 1 from t_tag t where t.entity_id = t_template.id and t.name in (%s)",
									Optional.ofNullable(repo.getTags()).orElse(new ArrayList<>()).stream()
											.map(x -> "'" + x + "'").collect(Collectors.joining(",")))));
			if (repo.getQuestionsNum() != null) {
				// 随机从问题里面挑选指定数量的题
				Collections.shuffle(repoTemplates);
				if (repoTemplates.size() > repo.getQuestionsNum()) {
					repoTemplates = repoTemplates.subList(0, repo.getQuestionsNum());
				}
			}

			// 给问题添加分值
			repoTemplates.forEach(template -> {
				if (templates.stream().filter(x -> x.getId().equals(template.getId())).findFirst().isPresent()) {
					return;
				}
				if (repo.getExamScore() != null) {
					if (template.getTemplate().getAttribute() == null) {
						template.getTemplate().setAttribute(new SurveySchema.Attribute());
					}
					template.getTemplate().getAttribute().setExamScore(repo.getExamScore());
				}
				templates.add(template);
			});
		});
		// 相同类型的问题排放在一起
		return templates.stream().map(x -> {
			SurveySchema schema = x.getTemplate();
			schema.setId(x.getId());
			return schema;
		}).sorted(Comparator.comparing(SurveySchema::getType)).collect(Collectors.toList());
	}

	@Override
	@SneakyThrows
	public void importFromTemplate(RepoTemplateRequest request) {
		request.setTemplates(parseExcelToTemplate(request.getFile()));
		batchAddRepoTemplate(request);
	}

	@SneakyThrows
	private List<TemplateRequest> parseExcelToTemplate(MultipartFile file) {
		return new RepoTemplateExcelParseHelper(file).parse();
	}

	@Override
	public PaginationResponse<UserBookView> listUserBook(UserBookQuery query) {
		Page<UserBook> page = userBookService.pageByQuery(query,
				Wrappers.<UserBook>lambdaQuery().like(query.getName() != null, UserBook::getName, query.getName())
						.eq(query.getType() != null, UserBook::getType, query.getType())
						.ge(query.getStartDate() != null, UserBook::getCreateAt, query.getStartDate())
						.le(query.getEndDate() != null, UserBook::getCreateAt, query.getEndDate())
						.eq(UserBook::getCreateBy, SecurityContextUtils.getUserId()));
		PaginationResponse<UserBookView> result = new PaginationResponse<>(page.getTotal(),
				userBookViewMapper.toView(page.getRecords()));
		return result;
	}

	@Override
	public void createUserBook(UserBookRequest request) {
		UserBook userBook = userBookViewMapper.fromRequest(request);
		userBookService.save(userBook);
	}

	@Override
	public void updateUserBook(UserBookRequest request) {
		UserBook userBook = userBookViewMapper.fromRequest(request);
		userBookService.updateById(userBook);
	}

	@Override
	public void deleteUserBook(UserBookRequest request) {
		if (request.getId() != null) {
			userBookService.removeById(request.getId());
		}
		if (CollectionUtils.isNotEmpty(request.getIds())) {
			userBookService.removeByIds(request.getIds());
		}
		if (request.getTemplateId() != null) {
			userBookService
					.remove(Wrappers.<UserBook>lambdaUpdate().eq(UserBook::getTemplateId, request.getTemplateId())
							.eq(UserBook::getCreateBy, SecurityContextUtils.getUserId()));
		}
	}

	@Override
	public List<RepoView> selectRepo(SelectRepoRequest request) {
		List<RepoView> result = repoViewMapper
				.toView(list(Wrappers.<Repo>lambdaQuery().eq(Repo::getMode, request.getMode()).and(x -> x
						.eq(Repo::getCreateBy, SecurityContextUtils.getUserId()).or(y -> y.eq(Repo::getShared, 1)))));
		result.forEach(repoView -> {
			repoView.setTotal(
					templateService.count(Wrappers.<Template>lambdaQuery().eq(Template::getRepoId, repoView.getId())));
			// 获取每个标签对应的题的数量
			repoView.setTemplateTags(this.getBaseMapper().selectRepoTemplateTags(repoView.getId()));
			// 获取每个问题类型对应的题的数量
			repoView.setRepoQuestionTypes(this.getBaseMapper().selectRepoQuestionTypes(repoView.getId()));
		});
		return result;
	}

}
