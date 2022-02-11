package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.core.uitls.ExcelExporter;
import cn.surveyking.server.core.uitls.SchemaParser;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.AnswerViewMapper;
import cn.surveyking.server.domain.model.Answer;
import cn.surveyking.server.domain.model.Project;
import cn.surveyking.server.mapper.AnswerMapper;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.DeptService;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.*;
import java.util.stream.Collectors;
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
				((Map<String, List<String>>) option2value).values().forEach(ids -> {
					if (questionType == SurveySchema.QuestionType.User) {
						view.setUsers(ids.stream().map(userId -> userService.loadUserById(userId).simpleMode())
								.collect(Collectors.toList()));
					}
					else if (questionType == SurveySchema.QuestionType.Signature
							|| questionType == SurveySchema.QuestionType.Upload) {
						FileQuery query = new FileQuery();
						query.setType(AppConsts.StorageType.ANSWER_ATTACHMENT);
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

	@Override
	public AnswerView getAnswer(AnswerQuery query) {
		AnswerView answerView = answerViewMapper.toAnswerView(getById(query.getId()));
		String projectId = answerView.getProjectId();
		FlatSurveySchemaByType schemaByType = parseSurveySchemaByType(projectMapper.selectById(projectId).getSurvey());
		setAnswerExtraInfo(answerView, schemaByType);
		return answerView;
	}

	@Override
	public String saveAnswer(AnswerRequest request, HttpServletRequest httpRequest) {
		request.getMetaInfo().setClientInfo(parseClientInfo(httpRequest));
		Answer answer = answerViewMapper.fromRequest(request);
		answer.setCreateAt(new Date());
		save(answer);
		return answer.getId();
	}

	@Override
	public void updateAnswer(AnswerRequest answer) {
		super.updateById(answerViewMapper.fromRequest(answer));
	}

	@Override
	public void deleteAnswer(String[] ids) {
		super.removeByIds(Arrays.asList(ids));
	}

	@Override
	public DownloadData downloadSurvey(String id) {
		Project project = projectMapper.selectById(id);
		List<SurveySchema> schemaDataTypes = SchemaParser.flatSurveySchema(project.getSurvey());

		AnswerQuery query = new AnswerQuery();
		query.setProjectId(id);
		query.setPageSize(Integer.MAX_VALUE);
		List<AnswerView> answerViews = listAnswer(query).getList();

		DownloadData download = new DownloadData();
		download.setFileName(project.getName() + ".xlsx");
		int[] indexArr = { 0 };
		try {
			PipedOutputStream outputStream = new PipedOutputStream();
			PipedInputStream inputStream = new PipedInputStream(outputStream);
			new Thread(() -> {
				try {
					new ExcelExporter.Builder().setSheetName(project.getName()).setOutputStream(outputStream)
							.setColumns(SchemaParser.parseColumns(schemaDataTypes))
							.setRows(answerViews.stream().map(answer -> {
								indexArr[0] = indexArr[0] += 1;
								return SchemaParser.parseRowData(answer, schemaDataTypes, indexArr[0]);
							}).collect(Collectors.toList())).build().exportToStream();
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
		answerQuery.setProjectId(query.getProjectId());
		answerQuery.setPageSize(Integer.MAX_VALUE);
		// 下载某个问卷答案的附件
		if (query.getAnswerId() != null) {
			answerQuery.setId(query.getAnswerId());
			return generateSurveyAttachment(listAnswer(answerQuery).getList().get(0));
		}
		else {
			// 下载所有问卷答案的附件
			downloadData.setResource(new InputStreamResource(answerAttachToZip(listAnswer(answerQuery).getList())));
			downloadData.setFileName(project.getName() + ".zip");
			downloadData.setMediaType(MediaType.parseMediaType("application/zip"));
		}
		return downloadData;
	}

	private DownloadData generateSurveyAttachment(AnswerView answer) {
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
			downloadData.setResource(new InputStreamResource(answerAttachToZip(Collections.singletonList(answer))));
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
	private InputStream answerAttachToZip(List<AnswerView> answers) {
		try {
			PipedOutputStream outputStream = new PipedOutputStream();
			PipedInputStream inputStream = new PipedInputStream(outputStream);
			new Thread(() -> {
				try (ZipOutputStream zout = new ZipOutputStream(outputStream);) {
					answers.forEach(answer -> {
						answer.getAttachment().forEach(attachment -> {
							ByteArrayResource resource = (ByteArrayResource) fileService
									.loadFile(new FileQuery(attachment.getId())).getBody();
							ZipEntry entry = new ZipEntry(answer.getId() + "_" + attachment.getOriginalName());
							try {
								zout.putNextEntry(entry);
								zout.write(resource.getByteArray());
							}
							catch (IOException e) {
								e.printStackTrace();
							}

						});
					});

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

}
