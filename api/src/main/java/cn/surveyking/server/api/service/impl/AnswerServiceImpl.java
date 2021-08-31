package cn.surveyking.server.api.service.impl;

import cn.surveyking.server.api.domain.dto.AnswerQuery;
import cn.surveyking.server.api.domain.dto.DownloadData;
import cn.surveyking.server.api.domain.dto.SurveySchemaType;
import cn.surveyking.server.api.domain.model.Answer;
import cn.surveyking.server.api.domain.model.Project;
import cn.surveyking.server.api.mapper.AnswerMapper;
import cn.surveyking.server.api.mapper.ProjectMapper;
import cn.surveyking.server.api.service.AnswerService;
import cn.surveyking.server.core.pagination.Page;
import cn.surveyking.server.core.uitls.SchemaParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
		answer.setUpdateAt(new Date());
		answerMapper.updateById(answer);
	}

	@Override
	public void deleteAnswer(String[] ids) {
		answerMapper.deleteBatchIds(Arrays.asList(ids));
	}

	@Override
	public DownloadData getDownloadData(String shortId) {
		QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("short_id", shortId).select("name", "survey");
		Project project = projectMapper.selectOne(queryWrapper);
		DownloadData download = new DownloadData();
		download.setFileName(project.getName());
		List<SurveySchemaType> schemaDataTypes = SchemaParser.parseDataTypes(project.getSurvey());
		download.setHeaderNames(schemaDataTypes.stream().map(x -> x.getTitle()).collect(Collectors.toList()));
		QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
		answerQuery.select("answer");
		List<Answer> answers = answerMapper.selectList(answerQuery);
		download.setRows(answers.stream().map(answer -> SchemaParser.parseRowData(answer.getAnswer(), schemaDataTypes))
				.collect(Collectors.toList()));
		return download;
	}

}
