package cn.surveyking.server.web.service.impl;

import cn.surveyking.server.web.domain.dto.AnswerQuery;
import cn.surveyking.server.web.domain.dto.DownloadData;
import cn.surveyking.server.web.domain.dto.DownloadQuery;
import cn.surveyking.server.web.domain.dto.SurveySchemaType;
import cn.surveyking.server.web.domain.model.Answer;
import cn.surveyking.server.web.domain.model.Project;
import cn.surveyking.server.web.mapper.AnswerMapper;
import cn.surveyking.server.web.mapper.ProjectMapper;
import cn.surveyking.server.web.service.AnswerService;
import cn.surveyking.server.web.service.FileService;
import cn.surveyking.server.core.exception.ServiceException;
import cn.surveyking.server.core.pagination.Page;
import cn.surveyking.server.core.uitls.ExcelExporter;
import cn.surveyking.server.core.uitls.SchemaParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
public class AnswerServiceImpl implements AnswerService {

	private final AnswerMapper answerMapper;

	private final ProjectMapper projectMapper;

	private final FileService fileService;

	@Override
	public IPage<Answer> listAnswer(AnswerQuery query) {
		QueryWrapper<Answer> wrapper = new QueryWrapper<>();
		wrapper.eq("short_id", query.getShortId()).orderByDesc("create_at");
		Page<Answer> page = new Page<>(query.getCurrent(), query.getPageSize());
		return answerMapper.selectPage(page, wrapper);
	}

	@Override
	public Answer getAnswer(AnswerQuery query) {
		QueryWrapper<Answer> wrapper = new QueryWrapper<>();
		wrapper.eq("id", query.getId());
		return answerMapper.selectOne(wrapper);
	}

	@Override
	public void saveAnswer(Answer answer, HttpServletRequest request) {
		answer.getMetaInfo().setClientInfo(parseClientInfo(request));
		answer.setCreateAt(new Date());
		answerMapper.insert(answer);
	}

	@Override
	public void updateAnswer(Answer answer) {
		answerMapper.updateById(answer);
	}

	@Override
	public void deleteAnswer(String[] ids) {
		answerMapper.deleteBatchIds(Arrays.asList(ids));
	}

	@Override
	public DownloadData downloadSurvey(String shortId) {
		QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("short_id", shortId).select("name", "survey");
		Project project = projectMapper.selectOne(queryWrapper);

		List<SurveySchemaType> schemaDataTypes = SchemaParser.parseDataTypes(project.getSurvey());
		QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
		answerQuery.select("id", "answer", "meta_info", "attachment", "create_at", "create_by");
		List<Answer> answers = answerMapper.selectList(answerQuery);

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
							.setRows(answers.stream().map(answer -> {
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
		QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("short_id", query.getShortId()).select("name", "survey");
		Project project = projectMapper.selectOne(queryWrapper);
		DownloadData downloadData = new DownloadData();
		// 下载某个问卷答案的附件
		if (query.getAnswerId() != null) {
			Answer answer = answerMapper.selectById(query.getAnswerId());
			return generateSurveyAttachment(answer);
		}
		else {
			// 下载所有问卷答案的附件
			QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
			answerQuery.eq("short_id", query.getShortId());
			downloadData.setResource(new InputStreamResource(answerAttachToZip(answerMapper.selectList(answerQuery))));
			downloadData.setFileName(project.getName() + ".zip");
			downloadData.setMediaType(MediaType.parseMediaType("application/zip"));
		}
		return downloadData;
	}

	private DownloadData generateSurveyAttachment(Answer answer) {
		DownloadData downloadData = new DownloadData();
		List<Answer.Attachment> attachments = answer.getAttachment();
		// 如果只有一个附件，则直接返回附件的结果
		if (attachments.size() == 1) {
			Answer.Attachment attachment = attachments.get(0);
			downloadData.setFileName(attachment.getOriginalName());
			downloadData.setResource(fileService.loadAsResource(attachment.getId()));
		}
		else {
			// 多个附件，压缩包
			downloadData.setResource(new InputStreamResource(answerAttachToZip(Arrays.asList(answer))));
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
	private InputStream answerAttachToZip(List<Answer> answers) {
		try {
			PipedOutputStream outputStream = new PipedOutputStream();
			PipedInputStream inputStream = new PipedInputStream(outputStream);
			new Thread(() -> {
				try (ZipOutputStream zout = new ZipOutputStream(outputStream);) {
					answers.forEach(answer -> {
						answer.getAttachment().forEach(attachment -> {
							byte[] buffer = new byte[1024 * 1024 * 1024];
							int count;
							ByteArrayResource resource = (ByteArrayResource) fileService
									.loadAsResource(attachment.getId());
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
					throw new ServiceException("生成压缩文件失败", e);
				}
			}).start();
			return inputStream;
		}
		catch (Exception e) {
			throw new ServiceException("生成压缩文件失败", e);
		}
	}

}
