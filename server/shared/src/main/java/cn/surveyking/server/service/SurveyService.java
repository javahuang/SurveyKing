package cn.surveyking.server.service;

import cn.surveyking.server.domain.dto.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javahuang
 * @date 2021/8/22
 */
public interface SurveyService {

	PublicProjectView loadProject(ProjectQuery projectQuery);

	PublicStatisticsView statProject(ProjectQuery query);

	PublicAnswerView saveAnswer(AnswerRequest answer, HttpServletRequest request);

	PublicQueryVerifyView loadQuery(PublicQueryRequest request);

	PublicQueryView getQueryResult(PublicQueryRequest request);

	PublicProjectView validateProject(ProjectQuery query);

	List<PublicDictView> loadDict(PublicDictRequest request);
}
