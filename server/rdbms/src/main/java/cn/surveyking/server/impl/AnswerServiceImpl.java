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
import cn.surveyking.server.mapper.AnswerMapper;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.service.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

	@Override
	public PaginationResponse<AnswerView> listAnswer(AnswerQuery query) {
		Page<Answer> page = new Page<>(query.getCurrent(), query.getPageSize());
		super.page(page,
				Wrappers.<Answer>lambdaQuery()
						.eq(query.getProjectId() != null, Answer::getProjectId, query.getProjectId())
						.in(query.getIds() != null && query.getIds().size() > 0, Answer::getId, query.getIds())
						.eq(query.getId() != null, Answer::getId, query.getId()).orderByDesc(Answer::getCreateAt));
		List<AnswerView> list = answerViewMapper.toAnswerView(page.getRecords());
		Project project = projectMapper.selectById(query.getProjectId());
		FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(project.getSurvey());
		list.forEach(view -> setAnswerExtraInfo(view, schemaByType));
		return new PaginationResponse<>(page.getTotal(), list);
	}

	private FlatSurveySchemaByType parseSurveySchemaByType(SurveySchema schema) {
		FlatSurveySchemaByType schemaByType = new FlatSurveySchemaByType();
		List<SurveySchema> schemaDataTypes = SchemaParser.flatSurveySchema(schema);
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
					}
					else if (questionType == SurveySchema.QuestionType.Signature
							|| questionType == SurveySchema.QuestionType.Upload) {
						FileQuery query = new FileQuery();
						query.setType(StorageTypeEnum.ANSWER_ATTACHMENT.getType());
						query.setIds(ids);
						// 图片上传和签名需要做一个合并
						view.getAttachment().addAll(fileService.listFiles(query));
					}
					else if (questionType == SurveySchema.QuestionType.Dept) {
						view.setDepts(ids
								.stream().map(id -> deptService.listDept(null).stream()
										.filter(x -> x.getId().equals(id)).findFirst().get())
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
			}
		}
	}

	@Override
	public AnswerView getAnswer(AnswerQuery query) {
		AnswerView answerView = null;
		if (query.getId() != null) {
			answerView = answerViewMapper.toAnswerView(getById(query.getId()));
		}
		else if (query.getProjectId() != null && Boolean.TRUE.equals(query.getLatest())) {
			answerView = answerViewMapper
					.toAnswerView(list(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, query.getProjectId())
							.eq(SecurityContextUtils.isAuthenticated(), Answer::getCreateBy,
									SecurityContextUtils.getUserId())
							.orderByDesc(Answer::getCreateAt)).stream().findFirst().orElse(null));
		}
		if (answerView == null) {
			return null;
		}
		String projectId = answerView.getProjectId();
		FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(projectMapper.selectById(projectId).getSurvey());
		setAnswerExtraInfo(answerView, schemaByType);
		return answerView;
	}

	@Override
	public AnswerView saveAnswer(AnswerRequest request, HttpServletRequest httpRequest) {
		// 公开查询修改答案时不会传元数据
		if (request.getMetaInfo() != null) {
			request.getMetaInfo().setClientInfo(parseClientInfo(httpRequest, request.getMetaInfo().getClientInfo()));
		}
		if (StringUtils.hasText(request.getId())) {
			return updateAnswer(request);
		}
		else {
			Answer answer = answerViewMapper.fromRequest(request);
			answer.setCreateAt(new Date());
			save(beforeSaveAnswer(answer));
			return answerViewMapper.toAnswerView(answer);
		}
	}

	@Override
	public long count(AnswerQuery query) {
		return count(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, query.getProjectId())
				.ge(query.getStartTime() != null, Answer::getCreateAt, query.getStartTime())
				.lt(query.getEndTime() != null, Answer::getCreateAt, query.getEndTime())
				.eq(query.getCreateBy() != null, Answer::getCreateBy, query.getCreateBy())
				.like(query.getIp() != null, Answer::getMetaInfo, query.getIp())
				.like(query.getCookie() != null, Answer::getMetaInfo, query.getCookie()));
	}

	@Override
	public AnswerView updateAnswer(AnswerRequest request) {
		Answer answer = beforeSaveAnswer(answerViewMapper.fromRequest(request));
		updateById(answer);
		return answerViewMapper.toAnswerView(answer);
	}

	@Override
	public void deleteAnswer(String[] ids) {
		super.removeByIds(Arrays.asList(ids));
	}

	@Override
	public DownloadData downloadSurvey(String id, Integer current, Integer pageSize, List<String> ids) {
		Project project = projectMapper.selectById(id);

		AnswerQuery query = new AnswerQuery();
		query.setProjectId(id);
		query.setIds(ids);
		if (current != null && pageSize != null) {
			query.setCurrent(current);
			query.setPageSize(pageSize);
		}else{
			query.setPageSize(Integer.MAX_VALUE);
		}
		List<AnswerView> answerViews = listAnswer(query).getList();

		DownloadData download = new DownloadData();
		download.setFileName(project.getName() + ".xlsx");
		try {
			PipedOutputStream outputStream = new PipedOutputStream();
			PipedInputStream inputStream = new PipedInputStream(outputStream);
			new Thread(() -> {
				try {
					export(project, answerViews, outputStream);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					try {
						outputStream.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			download.setResource(new InputStreamResource(inputStream));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		download.setMediaType(MediaType.parseMediaType("application/vnd.ms-excel"));
		return download;
	}

	/**
	 * 下载附件要使用 InputStreamResource，避免 Byte... 加载到内存
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
		if (query.getCurrent() != 0 && query.getPageSize() != 0) {
			answerQuery.setCurrent(query.getCurrent());
			answerQuery.setPageSize(query.getPageSize());
		}else{
			answerQuery.setPageSize(Integer.MAX_VALUE);
		}
		// 下载某个问卷答案的附件
		if (query.getAnswerId() != null) {
			answerQuery.setId(query.getAnswerId());
			return generateSurveyAttachment(project, listAnswer(answerQuery).getList().get(0));
		}
		else {
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
		List<AnswerView> list = answerViewMapper.toAnswerView(getBaseMapper().selectLogicDeleted(query.getProjectId()));
		Project project = projectMapper.selectById(query.getProjectId());
		FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(project.getSurvey());
		list.forEach(view -> setAnswerExtraInfo(view, schemaByType));
		return list;
	}

	@Override
	public void batchDestroyAnswer(String[] ids) {
		this.getBaseMapper().batchDestroy(Arrays.asList(ids));
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
					if (rowNum == 1 && Boolean.TRUE.equals(request.getAutoSchema())) {
						ProjectView projectView = parseRow2Schema(r, name);
						view.setProjectId(projectView.getId());
						view.setSchema(projectView.getSurvey());
					}
					else {
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

	private DownloadData generateSurveyAttachment(Project project, AnswerView answer) {
		DownloadData downloadData = new DownloadData();
		List<FileView> files = answer.getAttachment();
		// 如果只有一个附件，则直接返回附件的结果
		if (files.size() == 1) {
			FileView attachment = files.get(0);
			downloadData.setFileName(attachment.getOriginalName());
			downloadData.setResource(fileService.loadFile(new FileQuery(attachment.getId())).getBody());
		}
		else {
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
	 * @param answers
	 * @return
	 */
	private InputStream answerAttachToZip(Project project, List<AnswerView> answers, DownloadQuery query) {
		try {
			PipedOutputStream outputStream = new PipedOutputStream();
			PipedInputStream inputStream = new PipedInputStream(outputStream);
			new Thread(() -> {
				try (ZipOutputStream zout = new ZipOutputStream(outputStream);) {
					int[] serialNum = { 0, 0 };
					answers.forEach(answer -> {
						serialNum[1] = 0;
						answer.getAttachment().forEach(attachment -> {
							serialNum[0] += 1;
							serialNum[1] += 1;
							ByteArrayResource resource = (ByteArrayResource) fileService
									.loadFile(new FileQuery(attachment.getId())).getBody();
							String parsedFileName = parseAttachmentNameByExp(answer, query.getNameExp(),
									attachment.getOriginalName(), serialNum);
							ZipEntry entry = new ZipEntry(parsedFileName);
							try {
								zout.putNextEntry(entry);
								zout.write(resource.getByteArray());
							}
							catch (IOException e) {
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
				}
				catch (Exception e) {
					throw new InternalServerError("生成压缩文件失败", e);
				}
			}).start();
			return inputStream;
		}
		catch (Exception e) {
			throw new InternalServerError("生成压缩文件失败", e);
		}
	}

	private void export(Project project, List<AnswerView> answerViews, OutputStream outputStream) {
		List<SurveySchema> schemaDataTypes = SchemaParser.flatSurveySchema(project.getSurvey());
		int[] indexArr = { 0 };
		new ExcelExporter.Builder().setSheetName(project.getName()).setOutputStream(outputStream)
				.setRows(answerViews.stream().map(answer -> {
					indexArr[0] = indexArr[0] += 1;
					return SchemaParser.parseRowData(answer, schemaDataTypes, indexArr[0], project.getMode());
				}).collect(Collectors.toList()))
				.setColumns(SchemaParser.parseColumns(schemaDataTypes, project.getMode())).build().exportToStream();
	}

	/**
	 * @param answerView 当前答案
	 * @param nameExp 附件名称表达式
	 * @param nameExp 附件名称表达式
	 * @param serialNum 序号
	 * @return 新的附件名称
	 */
	private String parseAttachmentNameByExp(AnswerView answerView, String nameExp, String originalFileName,
			int[] serialNum) {
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
				}
				else if (AttachmentNameVariableEnum.serialNum.name().equals(questionId)) {
					expValue = serialNum[0] + "";
				}
				else if (AttachmentNameVariableEnum.serialNumInAnswer.name().equals(questionId)) {
					expValue = serialNum[1] + "";
				}
				else if (AttachmentNameVariableEnum.uploadDate.name().equals(questionId)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					expValue = sdf.format(answerView.getCreateAt());
				}
				else if (AttachmentNameVariableEnum.uploadDateTime.name().equals(questionId)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					expValue = sdf.format(answerView.getCreateAt());
				}
				else if (AttachmentNameVariableEnum.sourceName.name().equals(questionId)) {
					expValue = getFileNameWithoutSuffix(originalFileName);
				}
				else {
					// 问题变量
					Map<String, Object> questionValue = (Map<String, Object>) answerMap.get(questionId);

					if (questionValue != null) {
						// 单行文本表达式只有问题id #{xxxx}
						if (StringUtils.hasText(optionId)) {
							expValue = questionValue.get(optionId).toString();
						}
						else {
							// 多行文本会有选项id #{ancd.a3dx}
							expValue = questionValue.values().toArray()[0].toString();
						}
					}
				}

				fileName = StringUtils.replace(fileName, exp, expValue);
			}
			String suffix = getFileExtension(originalFileName);
			// 返回解析之后的文件名字
			if (suffix != null) {
				return fileName + "." + suffix;
			}
			return fileName;
		}
		else {
			// 返回原始文件名字
			return originalFileName;
		}
	}

	private void replaceWithVariable(String variableName) {

	}

	/**
	 * 获取文件后缀
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

	private Answer beforeSaveAnswer(Answer answer) {
		Project project = projectMapper.selectById(answer.getProjectId());
		if (ProjectModeEnum.exam.equals(project.getMode())) {
			AnswerScoreEvaluator evaluator = new AnswerScoreEvaluator(project.getSurvey(), answer.getAnswer());
			answer.setExamScore(evaluator.eval());
			AnswerExamInfo examInfo = new AnswerExamInfo();
			examInfo.setQuestionScore(evaluator.getQuestionScore());
			answer.setExamInfo(examInfo);
		}
		return answer;
	}

	private ProjectView parseRow2Schema(Row row, String name) {
		// 处理行头自动生成 schema
		SurveySchema schema = createSurveyFromExcelRowHeader(row);
		schema.setTitle(name);
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setSurvey(schema);
		projectRequest.setName(name);
		projectRequest.setMode(ProjectModeEnum.survey);
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

	private Answer parseRow2Answer(AnswerUploadView view, Row r) {
		Answer answer = new Answer();
		answer.setProjectId(view.getProjectId());
		LinkedHashMap<String, Map<String, String>> answerMap = new LinkedHashMap();
		int i = 0;
		for (SurveySchema questionSchema : view.getSchema().getChildren()) {
			String cellValue = r.getCellText(i);
			String questionId = questionSchema.getId();
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
