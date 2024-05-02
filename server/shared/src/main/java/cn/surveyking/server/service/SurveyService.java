package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/22
 */
public interface SurveyService {

    PublicProjectView loadProject(ProjectQuery projectQuery);

    PublicStatisticsView statProject(ProjectQuery query);

    PublicAnswerView saveAnswer(AnswerRequest request);

    PublicQueryVerifyView loadQuery(PublicQueryRequest request);

    PublicQueryView getQueryResult(PublicQueryRequest request);

    PublicProjectView validateProject(ProjectQuery query);

    List<PublicDictView> loadDict(PublicDictRequest request);

    PublicExamResult loadExamResult(PublicExamRequest request);

    void tempSaveAnswer(AnswerRequest request);

    PublicLinkResult loadLinkResult(PublicLinkRequest request);

    void validateProject(ProjectView project);

}
