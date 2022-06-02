package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectView;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.dto.SurveySchema;
import cn.surveyking.server.domain.model.Project;
import org.mapstruct.*;

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
	@Mapping(target = "setting.answerSetting.password", ignore = true)
	@Mapping(target = "setting.answerSetting.ipLimit", ignore = true)
	@Mapping(target = "setting.answerSetting.cookieLimit", ignore = true)
	@Mapping(target = "setting.answerSetting.loginLimit", ignore = true)
	PublicProjectView toPublicProjectView(ProjectView project);

	@AfterMapping
	default void calledWithSourceAndTargetType(ProjectView source, @MappingTarget PublicProjectView view) {
		// 非练习模式需要去掉 schema 里面的答案信息
		if (ProjectModeEnum.exam.equals(source.getMode())
				&& !((source.getSetting() != null && source.getSetting().getExamSetting() != null
						&& Boolean.TRUE.equals(source.getSetting().getExamSetting().getExerciseMode())))) {
			trimExamAnswerInfo(view.getSurvey());
		}
	}

	default void trimExamAnswerInfo(SurveySchema schema) {
		schema.getAttribute().setExamAnswerMode(null);
		schema.getAttribute().setExamCorrectAnswer(null);
		schema.getAttribute().setExamScore(null);
		schema.getAttribute().setExamMatchRule(null);
		if (schema.getChildren() != null) {
			schema.getChildren().forEach(sub -> trimExamAnswerInfo(sub));
		}
	}

}
