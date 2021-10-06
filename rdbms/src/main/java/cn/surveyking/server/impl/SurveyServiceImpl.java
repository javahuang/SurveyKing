package cn.surveyking.server.impl;

import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.domain.dto.ProjectQuery;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.domain.model.Project;
import cn.surveyking.server.mapper.ProjectMapper;
import cn.surveyking.server.service.SurveyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author javahuang
 * @date 2021/8/22
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {

	private final ProjectMapper projectMapper;

	private final ProjectViewMapper projectViewMapper;

	/**
	 * 如果需要验证密码，则只有密码输入正确之后才开始加载 schema
	 * @param query
	 * @return
	 */
	@Override
	public PublicProjectView loadProject(ProjectQuery query) {
		Project project = getProjectByShortId(query.getShortId());
		if (project != null && project.getSetting() != null && project.getSetting().getAnswerSetting() != null
				&& project.getSetting().getAnswerSetting().getPassword() != null) {
			project.getSetting().getAnswerSetting().setPassword(null);
			project.setSurvey(null);
		}
		return projectViewMapper.toPublicProjectView(project);
	}

	@Override
	public PublicProjectView verifyPassword(ProjectQuery query) {
		Project project = getProjectByShortId(query.getShortId());
		try {
			if (project.getSetting().getAnswerSetting().getPassword().equals(query.getPassword())) {
				return projectViewMapper.toPublicProjectView(project);
			}
			throw new InternalServerError("密码验证失败");
		}
		catch (Exception e) {
			throw new InternalServerError("密码验证失败");
		}
	}

	private Project getProjectByShortId(String shortId) {
		QueryWrapper<Project> queryMapper = new QueryWrapper();
		queryMapper.eq("short_id", shortId);
		return projectMapper.selectOne(queryMapper);
	}

}
