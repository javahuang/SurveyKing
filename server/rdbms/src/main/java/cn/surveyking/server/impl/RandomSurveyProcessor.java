package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 随机问题处理器
 * 负责处理考试模式下的随机问题选择和错题练习功能
 * 
 * @author javahuang
 * @date 2025-08-01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RandomSurveyProcessor {

    private final RepoServiceImpl repoService;
    private final AnswerServiceImpl answerService;
    private final ProjectViewMapper projectViewMapper;

    /**
     * 处理随机问题逻辑，包括随机问题和错题练习
     * 
     * @param project     项目信息
     * @param projectView 公开项目视图
     */
    public void processRandomSurvey(ProjectView project, PublicProjectView projectView) {
        if (ProjectModeEnum.exam != project.getMode()) {
            return;
        }

        SurveySchema source = project.getSurvey();
        boolean shouldSaveRandomSchema = false;

        // 处理编辑器中的 RandomSurvey 题型
        if (processRandomSurveyInSchema(source, projectView)) {
            shouldSaveRandomSchema = true;
        }

        // 处理随机抽题
        if (processRandomQuestionSelection(project, projectView, source)) {
            shouldSaveRandomSchema = true;
        }

        // 如果有 RandomSurvey 题型或配置了随机抽题，保存随机schema到答案表
        if (shouldSaveRandomSchema && projectView.getSurvey() != null) {
            saveRandomSchemaToAnswer(project, projectView.getSurvey(), projectView);
        }
    }

    /**
     * 处理编辑器中的RandomSurvey题型
     * 
     * @return true if RandomSurvey was processed, false otherwise
     */
    private boolean processRandomSurveyInSchema(SurveySchema source, PublicProjectView projectView) {
        if (source != null && !CollectionUtils.isEmpty(source.getChildren())) {
            List<SurveySchema> processedChildren = processRandomSurveyInChildren(source.getChildren());
            if (!processedChildren.equals(source.getChildren())) {
                SurveySchema updatedSource = SurveySchema.builder()
                        .id(source.getId())
                        .children(processedChildren)
                        .title(source.getTitle())
                        .attribute(source.getAttribute())
                        .description(source.getDescription())
                        .build();
                projectView.setSurvey(updatedSource);
                return true; // 表示处理了RandomSurvey题型
            }
        }
        return false; // 没有处理RandomSurvey题型
    }

    /**
     * 处理随机问题选择
     * 
     * @return true if random question selection was processed, false otherwise
     */
    private boolean processRandomQuestionSelection(ProjectView project, PublicProjectView projectView,
            SurveySchema source) {
        List<ProjectSetting.RandomSurveyCondition> randomSurveyCondition = project.getSetting().getExamSetting()
                .getRandomSurvey();

        if (randomSurveyCondition == null || randomSurveyCondition.isEmpty()) {
            return false;
        }

        // 尝试从已有答案中加载 schema
        SurveySchema existingSchema = loadSchemaFromExistingAnswer(project, projectView);
        if (existingSchema != null) {
            return false; // 已有答案，不需要重新处理
        }

        // 从题库中选择题目
        List<SurveySchema> selectedQuestions = selectQuestionsFromRepo(project, randomSurveyCondition);

        if (!selectedQuestions.isEmpty()) {
            SurveySchema randomSchema = buildRandomSchema(source, selectedQuestions, projectView);
            projectView.setSurvey(randomSchema);
            return true; // 表示处理了随机抽题
        }

        return false;
    }

    /**
     * 从已有答案中加载schema
     */
    private SurveySchema loadSchemaFromExistingAnswer(ProjectView project, PublicProjectView projectView) {
        String cookieName = AppConsts.COOKIE_RANDOM_PROJECT_PREFIX + project.getId();
        String answerId = ContextHelper.getCookie(cookieName);

        if (answerId == null) {
            return null;
        }

        try {
            AnswerQuery answerQuery = new AnswerQuery();
            answerQuery.setId(answerId);
            AnswerView answerView = answerService.getAnswer(answerQuery);

            if (answerView != null && answerView.getSurvey() != null) {
                projectView.setSurvey(answerView.getSurvey());
                projectView.setTempAnswer(answerView.getTempAnswer());
                return answerView.getSurvey();
            }
        } catch (Exception e) {
            log.warn("Failed to load existing answer schema for project {}: {}", project.getId(), e.getMessage());
        }

        return null;
    }

    /**
     * 从题库中选择题目
     */
    private List<SurveySchema> selectQuestionsFromRepo(ProjectView project,
            List<ProjectSetting.RandomSurveyCondition> randomSurveyCondition) {
        List<SurveySchema> questionSchemaList = repoService.pickQuestionFromRepo(randomSurveyCondition);

        // 合并编辑器中的问题和题库中的问题
        if (project.getSurvey() != null && !CollectionUtils.isEmpty(project.getSurvey().getChildren())) {
            questionSchemaList = Stream.concat(
                    project.getSurvey().getChildren().stream(),
                    questionSchemaList.stream())
                    .collect(Collectors.toList());
        }

        return questionSchemaList;
    }

    /**
     * 构建随机问卷结构
     */
    private SurveySchema buildRandomSchema(SurveySchema source, List<SurveySchema> questionSchemaList,
            PublicProjectView projectView) {
        SurveySchema randomSchema = SurveySchema.builder()
                .id(source.getId())
                .children(questionSchemaList)
                .title(source.getTitle())
                .attribute(source.getAttribute())
                .description(source.getDescription())
                .build();

        // 如果启用了随机排序，则随机化问题顺序
        if (shouldRandomizeOrder(projectView)) {
            projectViewMapper.randomSchemaOrder(randomSchema);
        }

        return randomSchema;
    }

    /**
     * 检查是否需要随机化问题顺序
     */
    private boolean shouldRandomizeOrder(PublicProjectView projectView) {
        return ProjectModeEnum.exam.equals(projectView.getMode())
                && projectView.getSetting() != null
                && projectView.getSetting().getExamSetting() != null
                && Boolean.TRUE.equals(projectView.getSetting().getExamSetting().getRandomOrder());
    }

    /**
     * 保存随机问卷到答案表并设置Cookie
     */
    private void saveRandomSchemaToAnswer(ProjectView project, SurveySchema randomSchema,
            PublicProjectView projectView) {
        projectView.setSurvey(randomSchema);

        try {
            AnswerRequest answerRequest = new AnswerRequest();
            answerRequest.setSurvey(randomSchema);
            answerRequest.setProjectId(project.getId());
            AnswerView answerView = answerService.saveAnswer(answerRequest);

            String cookieName = AppConsts.COOKIE_RANDOM_PROJECT_PREFIX + project.getId();
            Cookie cookie = new Cookie(cookieName, answerView.getId());
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7天过期
            ContextHelper.getCurrentHttpResponse().addCookie(cookie);

        } catch (Exception e) {
            log.error("Failed to save random schema to answer for project {}: {}", project.getId(), e.getMessage());
        }
    }

    /**
     * 递归处理Schema中的RandomSurvey题型
     * 
     * @param children 原始的children列表
     * @return 处理后的children列表
     */
    private List<SurveySchema> processRandomSurveyInChildren(List<SurveySchema> children) {
        List<SurveySchema> result = new ArrayList<>();

        for (SurveySchema child : children) {
            if (SurveySchema.QuestionType.RandomSurvey.equals(child.getType())) {
                // 处理RandomSurvey题型
                List<SurveySchema> randomQuestions = processSingleRandomSurvey(child);
                result.addAll(randomQuestions);
            } else {
                // 递归处理子节点
                if (!CollectionUtils.isEmpty(child.getChildren())) {
                    List<SurveySchema> processedChildren = processRandomSurveyInChildren(child.getChildren());
                    if (!processedChildren.equals(child.getChildren())) {
                        // 创建新的schema，更新children
                        SurveySchema newChild = SurveySchema.builder()
                                .id(child.getId())
                                .type(child.getType())
                                .title(child.getTitle())
                                .description(child.getDescription())
                                .attribute(child.getAttribute())
                                .children(processedChildren)
                                .build();
                        result.add(newChild);
                    } else {
                        result.add(child);
                    }
                } else {
                    result.add(child);
                }
            }
        }

        return result;
    }

    /**
     * 处理单个RandomSurvey题型，根据其配置的抽题规则选择对应的问题
     * 
     * @param randomSurveySchema RandomSurvey题型的schema
     * @return 随机选择的问题列表
     */
    private List<SurveySchema> processSingleRandomSurvey(SurveySchema randomSurveySchema) {
        try {
            // 获取RandomSurvey的抽题规则配置
            SurveySchema.Attribute attributeObj = randomSurveySchema.getAttribute();
            if (attributeObj == null) {
                return Collections.emptyList();
            }

            List<ProjectSetting.RandomSurveyCondition> conditions = attributeObj.getRandomSurvey();
            if (conditions.isEmpty()) {
                return Collections.emptyList();
            }

            // 使用repoService从题库中选择问题
            List<SurveySchema> selectedQuestions = repoService.pickQuestionFromRepo(conditions);
            return selectedQuestions != null ? selectedQuestions : Collections.emptyList();

        } catch (Exception e) {
            log.error("处理RandomSurvey题型时发生错误：{}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
