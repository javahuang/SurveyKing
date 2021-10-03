package cn.surveyking.server.web.service.impl;

import cn.surveyking.server.core.exception.ServiceException;
import cn.surveyking.server.core.uitls.NanoIdUtils;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.web.domain.dto.ProjectQuery;
import cn.surveyking.server.web.domain.dto.ProjectView;
import cn.surveyking.server.web.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.web.domain.model.Answer;
import cn.surveyking.server.web.domain.model.Project;
import cn.surveyking.server.web.domain.model.ProjectSetting;
import cn.surveyking.server.web.mapper.AnswerMapper;
import cn.surveyking.server.web.mapper.ProjectMapper;
import cn.surveyking.server.web.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/8/3
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectMapper projectMapper;

	private final AnswerMapper answerMapper;

	private final ProjectViewMapper projectViewMapper;

	@Override
	public List<ProjectView> listProject(ProjectQuery query) {
		QueryWrapper<Project> wrapper = new QueryWrapper<>();
		wrapper.eq("create_by", SecurityContextUtils.getUsername());
		wrapper.eq(isNotBlank(query.getName()), "name", query.getName());
		List<Project> projects = projectMapper.selectList(wrapper);
		List<ProjectView> result = projectViewMapper.toProjectView(projects);
		result.forEach(view -> {
			QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
			answerQueryWrapper.eq("short_id", view.getShortId());
			view.setTotal(answerMapper.selectCount(answerQueryWrapper));
		});
		return result;
	}

	public ProjectView getProject(ProjectQuery query) {
		QueryWrapper<Project> wrapper = new QueryWrapper<>();
		wrapper.eq("short_id", query.getShortId());
		Project project = projectMapper.selectOne(wrapper);
		ProjectView result = projectViewMapper.toProjectView(project);

		QueryWrapper<Answer> answerQuery = new QueryWrapper<>();
		answerQuery.eq("short_id", query.getShortId()).select("meta_info", "create_at").orderByDesc("create_at");
		List<Answer> answers = answerMapper.selectList(answerQuery);
		result.setTotal(answers.size());
		long totalDuration = 0;
		int totalOfToday = 0;
		for (int i = 0; i < answers.size(); i++) {
			Answer current = answers.get(i);
			if (i == 0) {
				result.setLastUpdate(current.getCreateAt().getTime());
			}
			if (current.getCreateAt().getTime() > LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
					.toEpochMilli()) {
				totalOfToday++;
			}
			totalDuration += current.getMetaInfo().getAnswerInfo().getEndTime()
					- current.getMetaInfo().getAnswerInfo().getStartTime();
		}
		if (totalDuration > 0) {
			result.setAverageDuration(totalDuration / answers.size());
			result.setTotalOfToday(totalOfToday);
		}
		return result;
	}

	@Override
	public String addProject(Project project) {
		project.setShortId(NanoIdUtils.randomNanoId());
		try {
			project.setName(project.getSurvey().getTitle());
			projectMapper.insert(project);
			return project.getShortId();
		}
		catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				return addProject(project);
			}
			else {
				throw new ServiceException(e);
			}
		}
	}

	@Override
	public void updateProject(Project project) {
		UpdateWrapper<Project> update = new UpdateWrapper<>();
		update.eq("short_id", project.getShortId());
		project.setUpdateAt(new Date());
		projectMapper.update(project, update);
	}

	@Override
	public void deleteProject(String id) {
		projectMapper.deleteById(id);
	}

	@Override
	public ProjectSetting getSetting(ProjectQuery query) {
		return null;
	}

}
