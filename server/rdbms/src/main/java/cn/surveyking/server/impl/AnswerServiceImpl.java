package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.AttachmentNameVariableEnum;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.constant.StorageTypeEnum;
import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.core.uitls.*;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.AnswerViewMapper;
import cn.surveyking.server.domain.model.Answer;
import cn.surveyking.server.domain.model.Project;
import cn.surveyking.server.domain.model.ProjectPartner;
import cn.surveyking.server.mapper.AnswerMapper;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.mapper.ProjectPartnerMapper;
import cn.surveyking.server.service.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author javahuang
 * @date 2021/8/3
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {

    private final ProjectMapper projectMapper;

    private final FileService fileService;

    private final AnswerViewMapper answerViewMapper;

    private final UserService userService;

    private final DeptService deptService;

    private final ProjectService projectService;

    private final ProjectPartnerMapper projectPartnerMapper;

    @Override
    public PaginationResponse<AnswerView> listAnswer(AnswerQuery query) {
        Page<Answer> page = new Page<>(query.getCurrent(), query.getPageSize());
        super.page(page, Wrappers.<Answer>lambdaQuery().isNotNull(Answer::getAnswer) // 暂存题答案会为空
                .eq(query.getProjectId() != null, Answer::getProjectId, query.getProjectId())
                .in(query.getIds() != null && query.getIds().size() > 0, Answer::getId, query.getIds())
                .lt(query.getEndTime() != null, Answer::getCreateAt, query.getEndTime())
                .gt(query.getStartTime() != null, Answer::getCreateAt, query.getStartTime())
                .eq(query.getId() != null, Answer::getId, query.getId()).orderByDesc(Answer::getCreateAt));
        List<AnswerView> list = answerViewMapper.toView(page.getRecords());
        Project project = projectMapper.selectById(query.getProjectId());
        FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(project.getSurvey());
        list.forEach(view -> setAnswerExtraInfo(view, schemaByType));
        return new PaginationResponse<>(page.getTotal(), list);
    }

    private FlatSurveySchemaByType parseSurveySchemaByType(SurveySchema schema) {
        FlatSurveySchemaByType schemaByType = new FlatSurveySchemaByType();
        List<SurveySchema> schemaDataTypes = SchemaHelper.flatSurveySchema(schema);
        schemaByType.setSchemaDataTypes(schemaDataTypes);
        schemaByType.setUserQuestions(parseSurveySchemaByType(schemaDataTypes, SurveySchema.QuestionType.User));
        schemaByType.setDeptQuestions(parseSurveySchemaByType(schemaDataTypes, SurveySchema.QuestionType.Dept));
        schemaByType.setFileQuestions(parseSurveySchemaByType(schemaDataTypes, SurveySchema.QuestionType.Signature));
        schemaByType.getFileQuestions()
                .addAll((parseSurveySchemaByType(schemaDataTypes, SurveySchema.QuestionType.Upload)));
        return schemaByType;
    }

    private List<SurveySchema> parseSurveySchemaByType(List<SurveySchema> flattedSurveySchema,
                                                       SurveySchema.QuestionType questionType) {
        return flattedSurveySchema.stream().filter(x -> x.getType() == questionType).collect(Collectors.toList());
    }

    private void setAnswerExtraInfo(AnswerView view, FlatSurveySchemaByType schemaByType) {
        setAnswerTypeInfo(schemaByType.getDeptQuestions(), view);
        setAnswerTypeInfo(schemaByType.getFileQuestions(), view);
        setAnswerTypeInfo(schemaByType.getUserQuestions(), view);
        setUserName(view);
    }

    private void setAnswerTypeInfo(List<SurveySchema> flatQuestionSchema, AnswerView view) {
        if (flatQuestionSchema.size() == 0) {
            return;
        }
        LinkedHashMap<String, Object> answers = view.getAnswer();
        SurveySchema.QuestionType questionType = flatQuestionSchema.get(0).getType();
        flatQuestionSchema.forEach(question -> {
            String questionId = question.getId();
            Object option2value = answers.get(questionId);
            if (option2value != null && option2value instanceof Map) {
                // 签名题前端存的是 {qId: {oId: fileId}} 需要转换成数组
                ((Map<String, Object>) option2value).values().stream().map(x -> {
                    if (x instanceof List) {
                        return (List<String>) x;
                    }
                    return Collections.singletonList(x.toString());
                }).collect(Collectors.toList()).forEach(ids -> {
                    if (questionType == SurveySchema.QuestionType.User) {
                        view.setUsers(ids.stream().map(userId -> userService.loadUserById(userId).simpleMode())
                                .collect(Collectors.toList()));
                    } else if (questionType == SurveySchema.QuestionType.Signature
                            || questionType == SurveySchema.QuestionType.Upload) {
                        FileQuery query = new FileQuery();
                        query.setType(StorageTypeEnum.ANSWER_ATTACHMENT.getType());
                        query.setIds(ids);
                        // 图片上传和签名需要做一个合并
                        view.getAttachment().addAll(fileService.listFiles(query));
                    } else if (questionType == SurveySchema.QuestionType.Dept) {
                        view.setDepts(ids
                                .stream().map(id -> deptService.listDept(null).stream()
                                        .filter(x -> x.getId().equals(id)).findFirst().orElseGet(DeptView::new))
                                .collect(Collectors.toList()));
                    }
                });
            }
        });
    }

    private void setUserName(AnswerView answerView) {
        if (answerView.getCreateBy() != null) {
            UserInfo userInfo = userService.loadUserById(answerView.getCreateBy());
            if (userInfo != null) {
                answerView.setCreateByName(userInfo.getName());
                return;
            }
            ProjectPartner projectPartner = projectPartnerMapper.selectById(answerView.getCreateBy());
            if (projectPartner != null) {
                answerView.setCreateByName(projectPartner.getUserName());
                return;
            }
        }
    }

    @Override
    public AnswerView getAnswer(AnswerQuery query) {
        AnswerView answerView = null;
        if (query.getId() != null) {
            answerView = answerViewMapper.toView(getById(query.getId()));
        } else if (query.getProjectId() != null && Boolean.TRUE.equals(query.getLatest())) {
            answerView = answerViewMapper.toView(list(Wrappers.<Answer>lambdaQuery()
                    .eq(Answer::getProjectId, query.getProjectId())
                    .eq(SecurityContextUtils.isAuthenticated(), Answer::getCreateBy, SecurityContextUtils.getUserId())
                    .eq(query.getCreateBy() != null, Answer::getCreateBy, query.getCreateBy())
                    .orderByDesc(Answer::getCreateAt)).stream().findFirst().orElse(null));
        }
        if (answerView == null) {
            return null;
        }
        // 获取考试排名信息
        if (query.isRankEnabled()) {
            List<Double> scores = list(Wrappers.<Answer>lambdaQuery().select(Answer::getExamScore, Answer::getId)
                    .eq(Answer::getProjectId, answerView.getProjectId())).stream()
                    .map(x -> Optional.ofNullable(x.getExamScore()).orElse(Double.valueOf(0)))
                    .collect(Collectors.toList());
            Collections.sort(scores, Collections.reverseOrder());
            answerView.setRank(scores.indexOf(answerView.getExamScore()) + 1);
        }
        String projectId = answerView.getProjectId();
        Project project = projectMapper.selectById(projectId);
        if (project == null || project.getSurvey() == null) {
            // 随机问题问卷没有 schema
            return answerView;
        }
        FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(project.getSurvey());
        setAnswerExtraInfo(answerView, schemaByType);
        return answerView;
    }

    @Override
    public AnswerView saveAnswer(AnswerRequest request) {
        // 公开查询修改答案时不会传元数据
        if (request.getMetaInfo() != null) {
            request.getMetaInfo().setClientInfo(parseClientInfo(request.getMetaInfo().getClientInfo()));
        }
        request.setTempSave(1);
        if (StringUtils.hasText(request.getId())) {
            return updateAnswer(request);
        } else {
            Answer answer = answerViewMapper.fromRequest(request);
            // 使用 uuid 作为外部公开查询引用，防暴力破解
            answer.setId(UUID.randomUUID().toString());
            answer.setCreateAt(new Date());
            save(beforeSaveAnswer(answer));
            return answerViewMapper.toView(answer);
        }
    }

    @Override
    public long count(AnswerQuery query) {
        return count(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, query.getProjectId())
                .ge(query.getStartTime() != null, Answer::getCreateAt, query.getStartTime())
                .lt(query.getEndTime() != null, Answer::getCreateAt, query.getEndTime())
                .eq(query.getCreateBy() != null, Answer::getCreateBy, query.getCreateBy())
                .like(query.getIp() != null, Answer::getMetaInfo, query.getIp())
                .like(query.getValueQuery() != null, Answer::getAnswer, query.getValueQuery())
                .like(query.getCookie() != null, Answer::getMetaInfo, query.getCookie()));
    }

    @Override
    public AnswerView updateAnswer(AnswerRequest request) {
        Answer answer = beforeSaveAnswer(answerViewMapper.fromRequest(request));
        updateById(answer);
        return answerViewMapper.toView(answer);
    }

    @Override
    public void deleteAnswer(AnswerRequest request) {
        super.removeByIds(request.getIds());
    }

    @Override
    public DownloadData downloadSurvey(DownloadQuery query) {
        Project project = projectMapper.selectById(query.getProjectId());

        AnswerQuery answerQuery = new AnswerQuery();
        answerQuery.setProjectId(query.getProjectId());
        answerQuery.setIds(query.getIds());
        answerQuery.setStartTime(query.getStartTime());
        answerQuery.setEndTime(query.getEndTime());
        if (query.getPageSize() != 0) {
            answerQuery.setCurrent(query.getCurrent());
            answerQuery.setPageSize(query.getPageSize());
        } else {
            answerQuery.setPageSize(Integer.MAX_VALUE);
        }
        List<AnswerView> answerViews = listAnswer(answerQuery).getList();

        DownloadData download = new DownloadData();
        download.setFileName(project.getName() + ".xlsx");
        try {
            PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream inputStream = new PipedInputStream(outputStream);
            new Thread(() -> {
                try {
                    export(project, answerViews, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            download.setResource(new InputStreamResource(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        download.setMediaType(MediaType.parseMediaType("application/vnd.ms-excel"));
        return download;
    }

    /**
     * 下载附件要使用 InputStreamResource，避免 Byte... 加载到内存
     *
     * @param query
     * @return
     */
    @Override
    public DownloadData downloadAttachment(DownloadQuery query) {
        Project project = projectMapper.selectById(query.getProjectId());
        DownloadData downloadData = new DownloadData();
        AnswerQuery answerQuery = new AnswerQuery();
        answerQuery.setIds(query.getIds());
        answerQuery.setProjectId(query.getProjectId());
        answerQuery.setStartTime(query.getStartTime());
        answerQuery.setEndTime(query.getEndTime());
        if (query.getCurrent() != 0 && query.getPageSize() != 0) {
            answerQuery.setCurrent(query.getCurrent());
            answerQuery.setPageSize(query.getPageSize());
        } else {
            answerQuery.setPageSize(Integer.MAX_VALUE);
        }
        // 下载某个问卷答案的附件
        if (query.getAnswerId() != null) {
            answerQuery.setId(query.getAnswerId());
            return generateSurveyAttachment(project, listAnswer(answerQuery).getList().get(0));
        } else {
            // 下载所有问卷答案的附件
            downloadData.setResource(
                    new InputStreamResource(answerAttachToZip(project, listAnswer(answerQuery).getList(), query)));
            downloadData.setFileName(project.getName() + ".zip");
            downloadData.setMediaType(MediaType.parseMediaType("application/zip"));
        }
        return downloadData;
    }

    @Override
    public List<AnswerView> listAnswerDeleted(AnswerQuery query) {
        List<AnswerView> list = answerViewMapper.toView(getBaseMapper().selectLogicDeleted(query.getProjectId()));
        Project project = projectMapper.selectById(query.getProjectId());
        FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(project.getSurvey());
        list.forEach(view -> setAnswerExtraInfo(view, schemaByType));
        return list;
    }

    @Override
    public void batchDestroyAnswer(AnswerRequest request) {
        this.getBaseMapper().batchDestroy(request.getIds());
    }

    @Override
    public void restoreAnswer(AnswerRequest request) {
        this.getBaseMapper().restoreAnswer(request.getIds());
    }

    @Override
    @SneakyThrows
    public AnswerUploadView upload(AnswerUploadRequest request) {
        AnswerUploadView view = new AnswerUploadView();
        try (InputStream is = request.getFile().getInputStream(); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();
            String name = request.getFile().getOriginalFilename().substring(0,
                    request.getFile().getOriginalFilename().lastIndexOf("."));
            List<Answer> answers = new ArrayList<>();
            try (Stream<Row> rows = sheet.openStream()) {
                // 第一行作为行头
                rows.forEach(r -> {
                    int rowNum = r.getRowNum();
                    if (rowNum == 1) {
                        if (request.getProjectId() != null) {
                            ProjectView projectView = projectService.getProject(request.getProjectId());
                            view.setProjectId(projectView.getId());
                            view.setSchema(filterSchemaByRow(r, projectView.getSurvey()));
                        } else if (Boolean.TRUE.equals(request.getAutoSchema())) {
                            ProjectView projectView = parseRow2Schema(r, name, request.getParentId());
                            view.setProjectId(projectView.getId());
                            view.setSchema(projectView.getSurvey());
                        }
                    } else {
                        // 处理答案
                        answers.add(parseRow2Answer(view, r));
                    }
                });
            }
            if (answers.size() > 0) {
                saveBatch(answers);
            }

        }
        return view;
    }

    @Override
    public PaginationResponse<ExerciseView> historyExercise(HistoryExerciseQuery query) {
        Page<Answer> page = new Page<>(query.getCurrent(), query.getPageSize());
        super.page(page, Wrappers.<Answer>lambdaQuery()
                .eq(query.getProjectId() != null, Answer::getProjectId, query.getProjectId())
                .isNotNull(Answer::getExamExerciseType)
                .eq(query.getTempSave() != null, Answer::getTempSave, query.getTempSave())
                .eq(Answer::getCreateBy, SecurityContextUtils.getUserId())
                .orderByDesc(Answer::getCreateAt));

        List<ExerciseView> list = page.getRecords().stream().map(x -> {
            ExerciseView view = new ExerciseView();
            view.setId(x.getId());
            view.setProjectName(x.getProjectId());
            view.setTempSave(x.getTempSave());
            view.setCreateAt(x.getCreateAt());
            view.setExamExerciseType(x.getExamExerciseType());
            view.setAnswerId(x.getId());
            SurveySchema schema = x.getSurvey();
            view.setProjectName(schema.getTitle());
            view.setRepoId(x.getRepoId());
            view.setPercent(0L);
            if (x.getSurvey() != null && x.getTempAnswer() != null && !x.getSurvey().getChildren().isEmpty()) {
                double percent = (double) x.getTempAnswer().size() / (double) x.getSurvey().getChildren().size() * 100;
                view.setPercent(Math.round(percent));
            }
            return view;
        }).collect(Collectors.toList());

        return new PaginationResponse<>(page.getTotal(), list);
    }

    private DownloadData generateSurveyAttachment(Project project, AnswerView answer) {
        DownloadData downloadData = new DownloadData();
        List<FileView> files = answer.getAttachment();
        // 如果只有一个附件，则直接返回附件的结果
        if (files.size() == 1) {
            FileView attachment = files.get(0);
            downloadData.setFileName(attachment.getOriginalName());
            downloadData.setResource(fileService.loadFile(new FileQuery(attachment.getId())).getBody());
        } else {
            // 多个附件，压缩包
            downloadData.setResource(new InputStreamResource(
                    answerAttachToZip(project, Collections.singletonList(answer), new DownloadQuery())));
            downloadData.setFileName(answer.getId() + ".zip");
            downloadData.setMediaType(MediaType.parseMediaType("application/zip"));
        }
        return downloadData;
    }

    /**
     * 将单个问卷的附件转成 zip 压缩包
     *
     * @param answers
     * @return
     */
    private InputStream answerAttachToZip(Project project, List<AnswerView> answers, DownloadQuery query) {
        try {
            PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream inputStream = new PipedInputStream(outputStream);
            new Thread(() -> {
                try (ZipOutputStream zout = new ZipOutputStream(outputStream);) {
                    int[] serialNum = {0, 0};
                    List<SurveySchema> uploadQuestions = SchemaHelper.flatSurveySchema(project.getSurvey()).stream()
                            .filter(qSchema -> SurveySchema.QuestionType.Upload.equals(qSchema.getType()))
                            .collect(Collectors.toList());
                    answers.forEach(answer -> {
                        serialNum[1] = 0;
                        answer.getAttachment().forEach(attachment -> {
                            serialNum[0] += 1;
                            serialNum[1] += 1;
                            ByteArrayResource resource = (ByteArrayResource) fileService
                                    .loadFile(new FileQuery(attachment.getId())).getBody();
                            String parsedFileName = parseAttachmentNameByExp(answer, query.getNameExp(), attachment,
                                    serialNum, uploadQuestions);
                            ZipEntry entry = new ZipEntry(parsedFileName);
                            try {
                                zout.putNextEntry(entry);
                                zout.write(resource.getByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
                    });
                    // 生成表格
                    if (DownloadQuery.DownloadType.answerAttachment.equals(query.getType())) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        export(project, answers, byteArrayOutputStream);
                        ZipEntry entry = new ZipEntry(project.getName() + ".xlsx");
                        zout.putNextEntry(entry);
                        zout.write(byteArrayOutputStream.toByteArray());
                    }
                } catch (Exception e) {
                    throw new InternalServerError("生成压缩文件失败", e);
                }
            }).start();
            return inputStream;
        } catch (Exception e) {
            throw new InternalServerError("生成压缩文件失败", e);
        }
    }

    private void export(Project project, List<AnswerView> answerViews, OutputStream outputStream) {
        List<SurveySchema> schemaDataTypes = SchemaHelper.flatSurveySchema(project.getSurvey());
        int[] indexArr = {0};
        new ExcelExporter.Builder().setSheetName(project.getName()).setOutputStream(outputStream)
                .setRows(answerViews.stream().map(answer -> {
                    indexArr[0] = indexArr[0] += 1;
                    return SchemaHelper.parseRowData(answer, schemaDataTypes, indexArr[0], project.getMode());
                }).collect(Collectors.toList()))
                .setColumns(SchemaHelper.parseColumns(schemaDataTypes, project.getMode())).build().exportToStream();
    }

    /**
     * @param answerView      当前答案
     * @param nameExp         附件名称表达式
     * @param file            当前附件
     * @param serialNum       序号
     * @param uploadQuestions 问卷上传题 schema
     * @return 新的附件名称
     */
    private String parseAttachmentNameByExp(AnswerView answerView, String nameExp, FileView file, int[] serialNum,
                                            List<SurveySchema> uploadQuestions) {
        if (StringUtils.hasText(nameExp)) {
            String fileName = nameExp;
            LinkedHashMap<String, Object> answerMap = answerView.getAnswer();
            Pattern pattern = Pattern.compile("#\\{([a-zA-Z0-9]+)\\.?([a-zA-Z0-9]*)\\}");
            Matcher matcher = pattern.matcher(nameExp);
            String exp, questionId, optionId = null;
            while (matcher.find()) {
                int count = matcher.groupCount();
                exp = matcher.group(0);
                questionId = matcher.group(1);
                if (count > 1) {
                    optionId = matcher.group(2);
                }
                String expValue = "";
                if (AttachmentNameVariableEnum.projectId.name().equals(questionId)) {
                    expValue = answerView.getProjectId();
                } else if (AttachmentNameVariableEnum.serialNum.name().equals(questionId)) {
                    expValue = serialNum[0] + "";
                } else if (AttachmentNameVariableEnum.serialNumInAnswer.name().equals(questionId)) {
                    expValue = serialNum[1] + "";
                } else if (AttachmentNameVariableEnum.uploadDate.name().equals(questionId)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    expValue = sdf.format(answerView.getCreateAt());
                } else if (AttachmentNameVariableEnum.uploadDateTime.name().equals(questionId)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    expValue = sdf.format(answerView.getCreateAt());
                } else if (AttachmentNameVariableEnum.sourceName.name().equals(questionId)) {
                    expValue = getFileNameWithoutSuffix(file.getOriginalName());
                } else if (AttachmentNameVariableEnum.questionTitle.name().equals(questionId)) {
                    for (SurveySchema uploadQuestion : uploadQuestions) {
                        Object qValue = answerView.getAnswer().get(uploadQuestion.getId());
                        if (qValue != null) {
                            boolean fileInCurrentSchema = ((Map<String, List<String>>) qValue)
                                    .get(uploadQuestion.getChildren().get(0).getId()).stream()
                                    .filter(x -> x.equals(file.getId())).findFirst().isPresent();
                            if (fileInCurrentSchema) {
                                expValue = uploadQuestion.getTitle();
                                break;
                            }
                        }
                    }
                } else {
                    // 问题变量
                    Map<String, Object> questionValue = (Map<String, Object>) answerMap.get(questionId);

                    if (questionValue != null) {
                        // 单行文本表达式只有问题id #{xxxx}
                        if (StringUtils.hasText(optionId)) {
                            expValue = questionValue.get(optionId).toString();
                        } else {
                            // 多行文本会有选项id #{ancd.a3dx}
                            expValue = questionValue.values().toArray()[0].toString();
                        }
                    }
                }

                fileName = StringUtils.replace(fileName, exp, expValue);
            }
            String suffix = getFileExtension(file.getOriginalName());
            // 返回解析之后的文件名字
            if (suffix != null) {
                return fileName + "." + suffix;
            }
            return fileName;
        } else {
            // 返回原始文件名字
            return file.getOriginalName();
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名称
     * @return 文件后缀
     */
    private String getFileExtension(String fileName) {
        String extension = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private String getFileNameWithoutSuffix(String fileName) {
        String fileNameWithoutSuffix = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fileNameWithoutSuffix = fileName.substring(0, i);
        }
        return fileNameWithoutSuffix;
    }

    /**
     * 答案保存之前计算考试分值、每题得分情况
     *
     * @param answer
     * @return
     */
    private Answer beforeSaveAnswer(Answer answer) {
        ProjectView project = projectService.getProject(answer.getProjectId());
        computeExamScore(answer, project);
        updateLinkSurveyAnswer(answer, project);
        return answer;
    }

    private void computeExamScore(Answer answer, ProjectView project) {
        if (project != null && ProjectModeEnum.exam.equals(project.getMode()) && answer != null
                && answer.getAnswer() != null && !ExerciseProjectTemplate.EXERCISE_PROJECT_ID.equals(project.getId())) {
            SurveySchema srcSchema = project.getSurvey();
            // 随机抽题需要根据答案获取 schema
            if (answer.getId() != null) {
                Answer existAnswer = getById(answer.getId());
                if (existAnswer != null && existAnswer.getSurvey() != null) {
                    srcSchema = existAnswer.getSurvey();
                }
            }
            AnswerScoreEvaluator evaluator = new AnswerScoreEvaluator(srcSchema, answer.getAnswer());
            answer.setExamScore(evaluator.eval());
            AnswerExamInfo examInfo = new AnswerExamInfo();
            examInfo.setQuestionScore(evaluator.getQuestionScore());
            answer.setExamInfo(examInfo);
        }
    }

    /**
     * 更新原始问卷答案
     *
     * @param answer
     * @param project
     */
    private void updateLinkSurveyAnswer(Answer answer, ProjectView project) {
        SchemaHelper.flatSurveySchema(project.getSurvey()).stream()
                .filter(qSchema -> !CollectionUtils.isEmpty(qSchema.getChildren().get(0).getLinkSurveys()))
                .forEach(qSchemaHasLinkSurvey -> {
                    Map<String, Object> qValue = (Map<String, Object>) answer.getAnswer()
                            .get(qSchemaHasLinkSurvey.getId());
                    if (qValue == null) {
                        return;
                    }
                    SurveySchema optionSchemaHasLinkAttr = qSchemaHasLinkSurvey.getChildren().get(0);
                    optionSchemaHasLinkAttr.getLinkSurveys().forEach(linkSurvey -> {
                        if (Boolean.TRUE.equals(linkSurvey.getEnableUpdate())) {
                            Object optionValue = qValue.get(optionSchemaHasLinkAttr.getId());
                            Answer linkedAnswer = this.getOne(Wrappers.<Answer>lambdaQuery()
                                    .eq(Answer::getProjectId, linkSurvey.getLinkSurveyId())
                                    .like(Answer::getAnswer,
                                            SchemaHelper.buildLinkLikeCondition(linkSurvey, optionValue))
                                    .orderByDesc(Answer::getCreateAt).last("limit 1"));
                            if (linkedAnswer != null) {
                                // 修改
                                for (SurveySchema.LinkField linkField : linkSurvey.getLinkFields()) {
                                    SchemaHelper.setQuestionValue(linkedAnswer.getAnswer(),
                                            linkField.getLinkQuestionId(), linkField.getLinkOptionId(),
                                            SchemaHelper.getQuestionValue(answer.getAnswer(),
                                                    linkField.getFillQuestionId(), linkField.getFillOptionId()));
                                }
                                this.updateById(linkedAnswer);
                            } else {
                                // 添加
                                Answer addAnswer = new Answer();
                                BeanUtils.copyProperties(answer, addAnswer, "id", "examScore", "examInfo", "answer",
                                        "projectId");
                                addAnswer.setProjectId(linkSurvey.getLinkSurveyId());
                                LinkedHashMap addAnswerMap = new LinkedHashMap();
                                addAnswer.setAnswer(addAnswerMap);
                                SchemaHelper.setQuestionValue(addAnswerMap, linkSurvey.getLinkQuestionId(),
                                        linkSurvey.getLinkOptionId(), optionValue);
                                for (SurveySchema.LinkField linkField : linkSurvey.getLinkFields()) {
                                    SchemaHelper.setQuestionValue(addAnswerMap, linkField.getLinkQuestionId(),
                                            linkField.getLinkOptionId(),
                                            SchemaHelper.getQuestionValue(answer.getAnswer(),
                                                    linkField.getFillQuestionId(), linkField.getFillOptionId()));
                                }
                                this.save(addAnswer);
                            }
                        }
                    });
                });
    }

    private ProjectView parseRow2Schema(Row row, String name, String parentId) {
        // 处理行头自动生成 schema
        SurveySchema schema = createSurveyFromExcelRowHeader(row);
        schema.setTitle(name);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setSurvey(schema);
        projectRequest.setName(name);
        projectRequest.setMode(ProjectModeEnum.survey);
        projectRequest.setParentId(parentId);
        projectRequest.setSetting(ProjectSetting.builder().mode(ProjectModeEnum.survey).status(1).build());
        return projectService.addProject(projectRequest);
    }

    private SurveySchema createSurveyFromExcelRowHeader(Row row) {
        Set<String> ids = new HashSet<>();
        SurveySchema schema = SurveySchema.builder()
                .children(row.stream()
                        .map(cell -> SurveySchema.builder().id(NanoIdUtils.randomNanoId(4, ids))
                                .type(SurveySchema.QuestionType.FillBlank).title(cell.getText())
                                .children(Collections.singletonList(
                                        SurveySchema.builder().id(NanoIdUtils.randomNanoId(4, ids)).build()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
        return schema;
    }

    private SurveySchema filterSchemaByRow(Row row, SurveySchema schema) {
        List<SurveySchema> flatSurveySchema = SchemaHelper.flatSurveySchema(schema);
        return SurveySchema.builder().id(schema.getId()).children(row.stream().map(cell -> {
            String title = cell.getText();
            return flatSurveySchema.stream().filter(x -> x.getTitle().equals(title)).findFirst()
                    .orElse(SurveySchema.builder().build());
        }).collect(Collectors.toList())).build();
    }

    private Answer parseRow2Answer(AnswerUploadView view, Row r) {
        Answer answer = new Answer();
        answer.setProjectId(view.getProjectId());
        LinkedHashMap<String, Map<String, String>> answerMap = new LinkedHashMap();
        int i = 0;
        for (SurveySchema questionSchema : view.getSchema().getChildren()) {
            String cellValue = r.getCellText(i);
            String questionId = questionSchema.getId();
            if (questionSchema.getChildren() == null || questionSchema.getChildren().size() == 0) {
                continue;
            }
            String optionId = questionSchema.getChildren().get(0).getId();
            if (cellValue != null) {
                Map<String, String> optionValue = new LinkedHashMap<>();
                optionValue.put(optionId, cellValue);
                answerMap.put(questionId, optionValue);
            }
            i++;
        }
        answer.setAnswer(answerMap);
        return answer;
    }

}
