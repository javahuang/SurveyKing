package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.uitls.SchemaHelper;
import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectView;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.dto.SurveySchema;
import cn.surveyking.server.domain.model.Project;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2021/8/25
 */
@Mapper
public interface ProjectViewMapper extends BaseModelMapper<ProjectRequest, ProjectView, Project> {

	@Mapping(target = "setting", source = "setting.answerSetting")
	@Mapping(target = "setting.answerSetting.password", ignore = true)
	@Mapping(target = "setting.answerSetting.ipLimit", ignore = true)
	@Mapping(target = "setting.answerSetting.cookieLimit", ignore = true)
	@Mapping(target = "setting.answerSetting.loginLimit", ignore = true)
	PublicProjectView toPublicProjectView(ProjectView project);

	@AfterMapping
	default void calledWithSourceAndTargetType(ProjectView source, @MappingTarget PublicProjectView view) {
		view.setSurvey(view.getSurvey().deepCopy());
		// 非练习模式需要去掉 schema 里面的答案信息
		if (ProjectModeEnum.exam.equals(source.getMode())
				&& !((source.getSetting() != null && source.getSetting().getExamSetting() != null
						&& Boolean.TRUE.equals(source.getSetting().getExamSetting().getExerciseMode())))) {
			trimExamAnswerInfo(view.getSurvey());
		}
		// 考试模式，随机问题顺序
		if (ProjectModeEnum.exam.equals(source.getMode())
				&& ((source.getSetting() != null && source.getSetting().getExamSetting() != null
						&& Boolean.TRUE.equals(source.getSetting().getExamSetting().getRandomOrder())))) {
			randomSchemaOrder(view.getSurvey());
		}
	}

	default void trimExamAnswerInfo(SurveySchema schema) {
		if (schema.getAttribute() == null) {
			schema.setAttribute(SurveySchema.Attribute.builder().build());
		}
		SchemaHelper.ignoreAttributes(schema, "examAnswerMode", "examCorrectAnswer", "examScore", "examMatchRule");
	}

	default void randomSchemaOrder(SurveySchema schema) {
		if (schema.getChildren() != null) {
			// 非考试题的顺序需要保持不变
			Map<SurveySchema, Integer> schemaShouldKeepOrder = new LinkedHashMap<>();
			for (int i = 0; i < schema.getChildren().size(); i++) {
				SurveySchema curr = schema.getChildren().get(i);
				if (curr.getAttribute().getExamAnswerMode() == SurveySchema.ExamScoreMode.none) {
					schemaShouldKeepOrder.put(curr, i);
				}
			}
			List<SurveySchema> schemasShouldReorder = schema.getChildren().stream()
					.filter(x -> !schemaShouldKeepOrder.containsKey(x)).collect(Collectors.toList());
			Collections.shuffle(schemasShouldReorder);
			schemaShouldKeepOrder.entrySet().stream().forEach(entry -> {
				schemasShouldReorder.add(entry.getValue(), entry.getKey());
			});
			schema.setChildren(schemasShouldReorder);

			schemasShouldReorder.forEach(child -> {
				if (schema.getAttribute().getExamAnswerMode() != SurveySchema.ExamScoreMode.none) {
					randomSchemaOrder(child);
				}
			});

		}
	}

}
