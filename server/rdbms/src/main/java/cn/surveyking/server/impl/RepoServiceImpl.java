package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.TagCategoryEnum;
import cn.surveyking.server.core.uitls.AnswerScoreEvaluator;
import cn.surveyking.server.core.uitls.RepoTemplateExcelParseHelper;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.core.uitls.ExcelExporter;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.RepoViewMapper;
import cn.surveyking.server.domain.mapper.UserBookViewMapper;
import cn.surveyking.server.domain.model.*;
import cn.surveyking.server.mapper.RepoMapper;
import cn.surveyking.server.service.AnswerService;
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
import java.util.Arrays;

import static cn.surveyking.server.impl.UserBookServiceImpl.BOOK_TYPE_WRONG;
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

    private final AnswerServiceImpl answerService;

    @Override
    public PaginationResponse<RepoView> listRepo(RepoQuery query) {
        Page<Repo> page = pageByQuery(query,
                Wrappers.<Repo>lambdaQuery().like(isNotBlank(query.getName()), Repo::getName, query.getName())
                        .eq(StringUtils.hasText(query.getCategory()), Repo::getCategory, query.getCategory())
                        .and(x -> x.eq(Repo::getCreateBy, SecurityContextUtils.getUserId())
                                .or(y -> y.eq(Repo::getShared, true)))
                        .eq(query.getIsPractice() != null, Repo::getIsPractice, query.getIsPractice())
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
            // 根据序号更新更新题库
            templateListOfCurrentRepo.stream().filter(t -> StringUtils.hasText(t.getSerialNo()) &&
                    t.getSerialNo().equals(template.getSerialNo())
                    && t.getQuestionType() == template.getQuestionType()).findFirst().ifPresent(t -> {
                        template.setId(t.getId());
                    });

            if (template.getId() == null) {
                template.setId(IdWorker.getIdStr());
                templatesAdd.add(template);
            } else {
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
     *
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
    public UserBookView updateUserBook(UserBookRequest request) {
        UserBookView result = new UserBookView();
        UserBook userBook = userBookViewMapper.fromRequest(request);
        if (userBook.getId() == null) {
            UserBook exist = userBookService.getOne(Wrappers.<UserBook>lambdaQuery()
                    .eq(UserBook::getTemplateId, request.getTemplateId())
                    .eq(UserBook::getCreateBy, SecurityContextUtils.getUserId())
                    .last("limit 1"));
            if (exist != null) {
                userBook.setId(exist.getId());
            }
        }
        if (request.getAnswer() != null) {
            Template template = templateService.getById(request.getTemplateId());
            userBook.setRepoId(template.getRepoId());
            userBook.setName(template.getName());
            userBook.setType(BOOK_TYPE_WRONG);
            // 模板问题分值默认是没有分数的，需要手动设置一个分数用于正确和错误运算
            template.getTemplate().getAttribute().setExamScore(5.0);
            template.getTemplate().setId(template.getId());
            AnswerScoreEvaluator answerScoreEvaluator = new AnswerScoreEvaluator(template.getTemplate(),
                    request.getAnswer());
            Double qScore = answerScoreEvaluator.eval();
            UserInfo userInfo = SecurityContextUtils.getUser();
            // 回答正确
            // 连续做对几次，自动移出错题/0代表永不移出
            if (qScore > 0) {
                userBook.setCorrectTimes(Optional.ofNullable(userBook.getCorrectTimes()).orElse(0) + 1);
                // 正确几次之后会从错题本移除
                if (Optional.ofNullable(userInfo.getCorrectTimes()).orElse(0) >= userBook.getCorrectTimes()) {
                    userBook.setWrongTimes(0);
                }
            } else {
                // 回答失败，只要做错一次就给正确次数置 0
                userBook.setWrongTimes(Optional.ofNullable(userBook.getWrongTimes()).orElse(0) + 1);
                userBook.setCorrectTimes(0);
            }

            result.setQscore(qScore);
            // 保存临时答案
            if (request.getAnswerId() != null) {
                Answer answer = answerService.getOne(Wrappers.<Answer>lambdaQuery().select(Answer::getId,
                        Answer::getTempAnswer, Answer::getExamInfo)
                        .eq(Answer::getId, request.getAnswerId()));
                LinkedHashMap tempAnswer = Optional.ofNullable(answer.getTempAnswer()).orElse(new LinkedHashMap());
                tempAnswer.putAll(request.getAnswer());
                answer.setTempAnswer(tempAnswer);
                AnswerExamInfo examInfo = Optional.ofNullable(answer.getExamInfo()).orElse(new AnswerExamInfo());
                LinkedHashMap<String, Double> questionScore = Optional.ofNullable(examInfo.getQuestionScore())
                        .orElse(new LinkedHashMap<>());
                examInfo.setQuestionScore(questionScore);
                questionScore.put(request.getTemplateId(), qScore);
                answer.setExamInfo(examInfo);
                answerService.updateById(answer);
            }
        }
        if (userBook.getId() != null) {
            userBookService.updateById(userBook);
        } else {
            userBookService.save(userBook);
        }

        return result;

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

    @Override
    @SneakyThrows
    public void exportRepoQuestions(RepoRequest request) {
        // 获取题库信息
        Repo repo = getById(request.getId());
        String fileName = (repo != null && repo.getName() != null ? repo.getName() : "题库") + ".xlsx";

        // 设置响应头
        ContextHelper.getCurrentHttpResponse()
                .setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ContextHelper.getCurrentHttpResponse().setHeader("Content-Disposition", "attachment; filename=" +
                java.net.URLEncoder.encode(fileName, "UTF-8"));

        // 查询题库中的各种题型
        List<Template> questions = templateService.list(Wrappers.<Template>lambdaQuery()
                .eq(Template::getRepoId, request.getId())
                .in(Template::getQuestionType, Arrays.asList(
                        SurveySchema.QuestionType.Radio,
                        SurveySchema.QuestionType.Checkbox,
                        SurveySchema.QuestionType.Judge,
                        SurveySchema.QuestionType.FillBlank,
                        SurveySchema.QuestionType.Textarea))
                .orderByAsc(Template::getQuestionType, Template::getCreateAt));

        // 准备导出数据 - 按题型分组
        Map<SurveySchema.QuestionType, List<Template>> questionsByType = questions.stream()
                .collect(Collectors.groupingBy(Template::getQuestionType));

        // 先处理单选题
        List<List<Object>> radioRows = new ArrayList<>();
        List<Template> radioQuestions = questionsByType.getOrDefault(SurveySchema.QuestionType.Radio,
                new ArrayList<>());
        int radioIndex = 1;

        for (Template template : radioQuestions) {
            SurveySchema schema = template.getTemplate();
            List<Object> row = new ArrayList<>();

            // 序号
            row.add(radioIndex++);

            // 题干
            row.add(schema.getTitle());

            // 选项A-H (单选题处理)
            List<SurveySchema> options = schema.getChildren();
            String[] optionTexts = new String[8];
            String correctAnswer = "";
            Double examScore = schema.getAttribute() != null ? schema.getAttribute().getExamScore() : null;

            if (options != null) {
                for (int i = 0; i < Math.min(options.size(), 8); i++) {
                    SurveySchema option = options.get(i);
                    optionTexts[i] = option.getTitle();

                    // 检查是否为正确答案（单选题只有一个正确答案）
                    if (option.getAttribute() != null &&
                            option.getAttribute().getExamCorrectAnswer() != null) {
                        correctAnswer = String.valueOf((char) ('A' + i));
                    }
                }
            }

            // 添加选项A-H
            for (int i = 0; i < 8; i++) {
                row.add(optionTexts[i] != null ? optionTexts[i] : "");
            }

            // 解析
            String analysis = schema.getAttribute() != null ? schema.getAttribute().getExamAnalysis() : "";
            row.add(analysis != null ? analysis : "");

            // 分数
            row.add(examScore != null ? examScore : "");

            // 答案
            row.add(correctAnswer);

            // 标签
            String tags = "";
            if (template.getTag() != null && template.getTag().length > 0) {
                tags = String.join(",", template.getTag());
            }
            row.add(tags);

            radioRows.add(row);
        }

        // 处理多选题
        List<List<Object>> checkboxRows = new ArrayList<>();
        List<Template> checkboxQuestions = questionsByType.getOrDefault(SurveySchema.QuestionType.Checkbox,
                new ArrayList<>());
        int checkboxIndex = 1;

        for (Template template : checkboxQuestions) {
            SurveySchema schema = template.getTemplate();
            List<Object> row = new ArrayList<>();

            // 序号
            row.add(checkboxIndex++);

            // 题干
            row.add(schema.getTitle());

            // 选项A-H (多选题处理)
            List<SurveySchema> options = schema.getChildren();
            String[] optionTexts = new String[8];
            List<String> correctAnswers = new ArrayList<>();
            Double examScore = schema.getAttribute() != null ? schema.getAttribute().getExamScore() : null;

            if (options != null) {
                for (int i = 0; i < Math.min(options.size(), 8); i++) {
                    SurveySchema option = options.get(i);
                    optionTexts[i] = option.getTitle();

                    // 检查是否为正确答案（多选题可能有多个正确答案）
                    if (option.getAttribute() != null &&
                            option.getAttribute().getExamCorrectAnswer() != null) {
                        correctAnswers.add(String.valueOf((char) ('A' + i)));
                    }
                }
            }

            // 添加选项A-H
            for (int i = 0; i < 8; i++) {
                row.add(optionTexts[i] != null ? optionTexts[i] : "");
            }

            // 解析
            String analysis = schema.getAttribute() != null ? schema.getAttribute().getExamAnalysis() : "";
            row.add(analysis != null ? analysis : "");

            // 分数
            row.add(examScore != null ? examScore : "");

            // 答案（多选题用逗号分隔多个答案）
            row.add(String.join(",", correctAnswers));

            // 标签
            String tags = "";
            if (template.getTag() != null && template.getTag().length > 0) {
                tags = String.join(",", template.getTag());
            }
            row.add(tags);

            checkboxRows.add(row);
        }

        // 处理判断题
        List<List<Object>> judgeRows = new ArrayList<>();
        List<Template> judgeQuestions = questionsByType.getOrDefault(SurveySchema.QuestionType.Judge,
                new ArrayList<>());
        int judgeIndex = 1;

        for (Template template : judgeQuestions) {
            SurveySchema schema = template.getTemplate();
            List<Object> row = new ArrayList<>();

            // 序号
            row.add(judgeIndex++);

            // 题干
            row.add(schema.getTitle());

            // 选项A、选项B（判断题一般是正确/错误）
            List<SurveySchema> options = schema.getChildren();
            String optionA = "";
            String optionB = "";
            String correctAnswer = "";
            Double examScore = schema.getAttribute() != null ? schema.getAttribute().getExamScore() : null;

            if (options != null && options.size() >= 2) {
                optionA = options.get(0).getTitle();
                optionB = options.get(1).getTitle();

                // 检查正确答案
                if (options.get(0).getAttribute() != null &&
                        options.get(0).getAttribute().getExamCorrectAnswer() != null) {
                    correctAnswer = "A";
                } else if (options.get(1).getAttribute() != null &&
                        options.get(1).getAttribute().getExamCorrectAnswer() != null) {
                    correctAnswer = "B";
                }
            }

            row.add(optionA);
            row.add(optionB);

            // 解析
            String analysis = schema.getAttribute() != null ? schema.getAttribute().getExamAnalysis() : "";
            row.add(analysis != null ? analysis : "");

            // 分数
            row.add(examScore != null ? examScore : "");

            // 答案
            row.add(correctAnswer);

            // 标签
            String tags = "";
            if (template.getTag() != null && template.getTag().length > 0) {
                tags = String.join(" ", template.getTag());
            }
            row.add(tags);

            judgeRows.add(row);
        }

        // 处理填空题
        List<List<Object>> fillBlankRows = new ArrayList<>();
        List<Template> fillBlankQuestions = questionsByType.getOrDefault(SurveySchema.QuestionType.FillBlank,
                new ArrayList<>());
        int fillBlankIndex = 1;

        for (Template template : fillBlankQuestions) {
            SurveySchema schema = template.getTemplate();
            List<Object> row = new ArrayList<>();

            // 序号
            row.add(fillBlankIndex++);

            // 题干
            row.add(schema.getTitle());

            // 空1-空8
            List<SurveySchema> blanks = schema.getChildren();
            String[] blankAnswers = new String[8];
            Double examScore = schema.getAttribute() != null ? schema.getAttribute().getExamScore() : null;

            if (blanks != null) {
                for (int i = 0; i < Math.min(blanks.size(), 8); i++) {
                    SurveySchema blank = blanks.get(i);
                    if (blank.getAttribute() != null && blank.getAttribute().getExamCorrectAnswer() != null) {
                        blankAnswers[i] = blank.getAttribute().getExamCorrectAnswer();
                    }
                }
            }

            // 添加空1-空8
            for (int i = 0; i < 8; i++) {
                row.add(blankAnswers[i] != null ? blankAnswers[i] : "");
            }

            // 解析
            String analysis = schema.getAttribute() != null ? schema.getAttribute().getExamAnalysis() : "";
            row.add(analysis != null ? analysis : "");

            // 单空分数
            row.add(examScore != null ? examScore : "");

            // 标签
            String tags = "";
            if (template.getTag() != null && template.getTag().length > 0) {
                tags = String.join(" ", template.getTag());
            }
            row.add(tags);

            fillBlankRows.add(row);
        }

        // 处理简答题（Textarea）
        List<List<Object>> textareaRows = new ArrayList<>();
        List<Template> textareaQuestions = questionsByType.getOrDefault(SurveySchema.QuestionType.Textarea,
                new ArrayList<>());
        int textareaIndex = 1;

        for (Template template : textareaQuestions) {
            SurveySchema schema = template.getTemplate();
            List<Object> row = new ArrayList<>();

            // 序号
            row.add(textareaIndex++);

            // 题干
            row.add(schema.getTitle());

            // 答案（对于简答题，可能存储在第一个子元素中）
            String answer = "";
            if (schema.getChildren() != null && !schema.getChildren().isEmpty()) {
                SurveySchema firstChild = schema.getChildren().get(0);
                if (firstChild.getAttribute() != null && firstChild.getAttribute().getExamCorrectAnswer() != null) {
                    answer = firstChild.getAttribute().getExamCorrectAnswer();
                }
            }
            row.add(answer);

            // 解析
            String analysis = schema.getAttribute() != null ? schema.getAttribute().getExamAnalysis() : "";
            row.add(analysis != null ? analysis : "");

            // 分数
            Double examScore = schema.getAttribute() != null ? schema.getAttribute().getExamScore() : null;
            row.add(examScore != null ? examScore : "");

            // 标签
            String tags = "";
            if (template.getTag() != null && template.getTag().length > 0) {
                tags = String.join(" ", template.getTag());
            }
            row.add(tags);

            textareaRows.add(row);
        }

        // 准备不同题型的列标题
        List<String> radioCheckboxHeaders = Arrays.asList(
                "序号", "题干", "选项A", "选项B", "选项C", "选项D",
                "选项E", "选项F", "选项G", "选项H", "解析", "分数", "答案", "标签");

        List<String> judgeHeaders = Arrays.asList(
                "序号", "题干", "选项A", "选项B", "解析", "分数", "答案", "标签");

        List<String> fillBlankHeaders = Arrays.asList(
                "序号", "题干", "空1", "空2", "空3", "空4", "空5", "空6", "空7", "空8", "解析", "单空分数", "标签");

        List<String> textareaHeaders = Arrays.asList(
                "序号", "题干", "答案", "解析", "分数", "标签");

        // 创建Excel工作簿，包含多个sheet
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            org.dhatim.fastexcel.Workbook workbook = new org.dhatim.fastexcel.Workbook(baos, "题库", "1.0");

            // 创建单选题sheet
            if (!radioRows.isEmpty()) {
                org.dhatim.fastexcel.Worksheet radioSheet = workbook.newWorksheet("单选题");
                radioSheet.fitToWidth((short) 10);
                radioSheet.setFitToPage(true);

                // 添加表头
                for (int i = 0; i < radioCheckboxHeaders.size(); i++) {
                    radioSheet.value(0, i, radioCheckboxHeaders.get(i));
                }

                // 添加数据行
                for (int r = 0; r < radioRows.size(); r++) {
                    List<Object> rowData = radioRows.get(r);
                    for (int c = 0; c < rowData.size(); c++) {
                        Object value = rowData.get(c);
                        if (value instanceof Integer) {
                            radioSheet.value(r + 1, c, (Number) value);
                        } else if (value instanceof String) {
                            radioSheet.value(r + 1, c, (String) value);
                        } else if (value != null) {
                            radioSheet.value(r + 1, c, value.toString());
                        }
                    }
                }
            }

            // 创建多选题sheet
            if (!checkboxRows.isEmpty()) {
                org.dhatim.fastexcel.Worksheet checkboxSheet = workbook.newWorksheet("多选题");
                checkboxSheet.fitToWidth((short) 10);
                checkboxSheet.setFitToPage(true);

                // 添加表头
                for (int i = 0; i < radioCheckboxHeaders.size(); i++) {
                    checkboxSheet.value(0, i, radioCheckboxHeaders.get(i));
                }

                // 添加数据行
                for (int r = 0; r < checkboxRows.size(); r++) {
                    List<Object> rowData = checkboxRows.get(r);
                    for (int c = 0; c < rowData.size(); c++) {
                        Object value = rowData.get(c);
                        if (value instanceof Integer) {
                            checkboxSheet.value(r + 1, c, (Number) value);
                        } else if (value instanceof String) {
                            checkboxSheet.value(r + 1, c, (String) value);
                        } else if (value != null) {
                            checkboxSheet.value(r + 1, c, value.toString());
                        }
                    }
                }
            }

            // 创建判断题sheet
            if (!judgeRows.isEmpty()) {
                org.dhatim.fastexcel.Worksheet judgeSheet = workbook.newWorksheet("判断题");
                judgeSheet.fitToWidth((short) 10);
                judgeSheet.setFitToPage(true);

                // 添加表头
                for (int i = 0; i < judgeHeaders.size(); i++) {
                    judgeSheet.value(0, i, judgeHeaders.get(i));
                }

                // 添加数据行
                for (int r = 0; r < judgeRows.size(); r++) {
                    List<Object> rowData = judgeRows.get(r);
                    for (int c = 0; c < rowData.size(); c++) {
                        Object value = rowData.get(c);
                        if (value instanceof Integer) {
                            judgeSheet.value(r + 1, c, (Number) value);
                        } else if (value instanceof String) {
                            judgeSheet.value(r + 1, c, (String) value);
                        } else if (value != null) {
                            judgeSheet.value(r + 1, c, value.toString());
                        }
                    }
                }
            }

            // 创建填空题sheet
            if (!fillBlankRows.isEmpty()) {
                org.dhatim.fastexcel.Worksheet fillBlankSheet = workbook.newWorksheet("填空题");
                fillBlankSheet.fitToWidth((short) 10);
                fillBlankSheet.setFitToPage(true);

                // 添加表头
                for (int i = 0; i < fillBlankHeaders.size(); i++) {
                    fillBlankSheet.value(0, i, fillBlankHeaders.get(i));
                }

                // 添加数据行
                for (int r = 0; r < fillBlankRows.size(); r++) {
                    List<Object> rowData = fillBlankRows.get(r);
                    for (int c = 0; c < rowData.size(); c++) {
                        Object value = rowData.get(c);
                        if (value instanceof Integer) {
                            fillBlankSheet.value(r + 1, c, (Number) value);
                        } else if (value instanceof String) {
                            fillBlankSheet.value(r + 1, c, (String) value);
                        } else if (value != null) {
                            fillBlankSheet.value(r + 1, c, value.toString());
                        }
                    }
                }
            }

            // 创建简答题sheet
            if (!textareaRows.isEmpty()) {
                org.dhatim.fastexcel.Worksheet textareaSheet = workbook.newWorksheet("简答题");
                textareaSheet.fitToWidth((short) 10);
                textareaSheet.setFitToPage(true);

                // 添加表头
                for (int i = 0; i < textareaHeaders.size(); i++) {
                    textareaSheet.value(0, i, textareaHeaders.get(i));
                }

                // 添加数据行
                for (int r = 0; r < textareaRows.size(); r++) {
                    List<Object> rowData = textareaRows.get(r);
                    for (int c = 0; c < rowData.size(); c++) {
                        Object value = rowData.get(c);
                        if (value instanceof Integer) {
                            textareaSheet.value(r + 1, c, (Number) value);
                        } else if (value instanceof String) {
                            textareaSheet.value(r + 1, c, (String) value);
                        } else if (value != null) {
                            textareaSheet.value(r + 1, c, value.toString());
                        }
                    }
                }
            }

            workbook.finish();

            // 写入响应流
            ContextHelper.getCurrentHttpResponse().getOutputStream().write(baos.toByteArray());
        }
    }

}
