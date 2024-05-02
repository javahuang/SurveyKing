package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ProjectModeEnum;

/**
 * 练习模式项目模板
 */
public class ExerciseProjectTemplate {

    public static final String EXERCISE_PROJECT_ID = "exercise";

    public static ProjectView getExerciseTemplate() {
        ProjectView projectView = new ProjectView();
        projectView.setId(EXERCISE_PROJECT_ID);
        projectView.setSurvey(SurveySchema.builder()
                        .id(EXERCISE_PROJECT_ID)
                .build());
        ProjectSetting setting = new ProjectSetting();
        setting.setStatus(1);
        projectView.setSetting(setting);
        ProjectSetting.ExamSetting examSetting = new ProjectSetting.ExamSetting();
        setting.setExamSetting(examSetting);
        ProjectSetting.AnswerSetting answerSetting = new ProjectSetting.AnswerSetting();
        answerSetting.setAnswerSheetVisible(true);
        answerSetting.setOnePageOneQuestion(true);
        answerSetting.setQuestionNumber(true);
        answerSetting.setProgressBar(true);
        setting.setAnswerSetting(answerSetting);
        projectView.setStatus(1);
        projectView.setMode(ProjectModeEnum.exam);
        return projectView;
    }
}
