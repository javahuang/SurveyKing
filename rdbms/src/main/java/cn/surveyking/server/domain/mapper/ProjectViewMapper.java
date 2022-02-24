package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectView;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/25
 */
@Mapper
public interface ProjectViewMapper {

	List<ProjectView> toProjectView(List<Project> projects);

	ProjectView toProjectView(Project project);

	Project fromRequest(ProjectRequest projectRequest);

	@Mapping(target = "setting", source = "setting.answerSetting")
	@Mapping(target = "passwordRequired", expression = "java(project.getSurvey() == null)")
	@Mapping(target = "setting.answerSetting.password", ignore = true)
	// @Mapping(target = "setting.answerSetting.maxAnswers", ignore = true)
	PublicProjectView toPublicProjectView(Project project);

}
