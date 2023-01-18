package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.Tuple2;
import cn.surveyking.server.core.constant.*;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.security.JwtTokenUtil;
import cn.surveyking.server.core.uitls.*;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.domain.model.Answer;
import cn.surveyking.server.domain.model.CommDictItem;
import cn.surveyking.server.domain.model.ProjectPartner;
import cn.surveyking.server.domain.model.UserBook;
import cn.surveyking.server.mapper.ProjectPartnerMapper;
import cn.surveyking.server.service.ProjectService;
import cn.surveyking.server.service.SurveyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/8/22
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

	private final ProjectService projectService;

	private final ProjectViewMapper projectViewMapper;

	private final AnswerServiceImpl answerService;

	private final ProjectPartnerMapper projectPartnerMapper;

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	private final DictItemServiceImpl dictItemService;

	private final RepoServiceImpl repoService;

	private final UserBookServiceImpl userBookService;

	private final TemplateServiceImpl templateService;

	private final ObjectMapper objectMapper;

	/**
	 * answerService 如果需要验证密码，则只有密码输入正确之后才开始加载 schema
	 * @param query
	 * @return
	 */
	@Override
	public PublicProjectView loadProject(ProjectQuery query) {
		ProjectView project = projectService.getProject(query.getId());
		PublicProjectView projectView = projectViewMapper.toPublicProjectView(project);
		if (project == null) {
			throw new ErrorCodeException(ErrorCode.ProjectNotFound);
		}
		SurveySchema loginFormSchema = null;
		if (query.getAnswerId() == null) {
			// 表单需要验证
			loginFormSchema = convertAndValidateLoginFormIfNeeded(project, null);
			// 校验问卷
			validateProject(project);
		}
		else {
			// 直接根据答案加载出 schema
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setId(query.getAnswerId());
			AnswerView answerView = answerService.getAnswer(answerQuery);
			if (answerView != null) {
				projectView.setAnswer(answerView.getAnswer());
				if (answerView.getSurvey() != null) {
					// 随机问题
					projectView.setSurvey(answerView.getSurvey());
				}
			}
			return projectView;
		}

		// 如果需要登录，将问卷 schema 替换成登录表单的 schema
		if (loginFormSchema != null) {
			projectView.setSurvey(loginFormSchema);
			projectView.setLoginRequired(true);
		}
		else {
			// 随机问题
			replaceSchemaIfRandomSchema(project, projectView);
			// 允许修改答案
			projectView.setAnswer(getLatestAnswer(projectView, null));
		}
		projectView.setIsAuthenticated(SecurityContextUtils.isAuthenticated());
		return projectView;
	}

	/**
	 * 验证问卷
	 * @param query
	 * @return
	 */
	@Override
	public PublicProjectView validateProject(ProjectQuery query) {
		String projectId = query.getId();
		ProjectView project = projectService.getProject(projectId);
		// 登录验证
		convertAndValidateLoginFormIfNeeded(project, query.getAnswer());
		PublicProjectView projectView = projectViewMapper.toPublicProjectView(project);
		// 随机问题
		replaceSchemaIfRandomSchema(project, projectView);
		// 校验问卷
		validateProject(project);
		projectView.setAnswer(getLatestAnswer(projectView, (String) SchemaHelper.getLoginFormAnswer(query.getAnswer(),
				SchemaHelper.LoginFormFieldEnum.whitelistName)));
		return projectView;
	}

	@Override
	public PublicStatisticsView statProject(ProjectQuery query) {
		AnswerQuery answerQuery = new AnswerQuery();
		answerQuery.setProjectId(query.getId());
		answerQuery.setPageSize(-1);
		List<AnswerView> answers = answerService.listAnswer(answerQuery).getList();
		ProjectView project = projectService.getProject(query.getId());
		return new ProjectStatHelper(project.getSurvey(), answers).stat();
	}

	@Override
	public PublicAnswerView saveAnswer(AnswerRequest request) {
		String projectId = request.getProjectId();
		PublicAnswerView result = new PublicAnswerView();
		ProjectView project = projectService.getProject(projectId);
		String answerId = request.getId();
		// 随机问卷更新答案
		String randomSurveyCookieName = AppConsts.COOKIE_RANDOM_PROJECT_PREFIX + project.getId();
		String answerIdFromCookie = ContextHelper.getCookie(randomSurveyCookieName);
		if (isNotBlank(answerIdFromCookie)) {
			answerId = answerIdFromCookie;
		}
		else if (isNotBlank(request.getQueryId())) {
			// 公开查询修改答案
			validateAndMergeAnswer(project, request);
		}
		else if (isNotBlank(request.getId())) {
			// 传入答案ID 并且设置了允许修改答案，则可以修改问卷答案
			boolean enableUpdate = Boolean.TRUE.equals(project.getSetting().getSubmittedSetting().getEnableUpdate());
			if (!enableUpdate) {
				throw new ErrorCodeException(ErrorCode.AnswerChangeDisabled);
			}
		}
		else {
			// 问卷允许修改答案 开关修改答案
			AnswerView latestAnswer = validateAndGetLatestAnswer(project, request);
			if (latestAnswer != null) {
				answerId = latestAnswer.getId();
			}
		}

		// 保存答案
		request.setId(answerId);
		AnswerView answerView = answerService.saveAnswer(request);
		result.setAnswerId(answerView.getId());
		// 考试模式，计算分值传给前端
		if (ProjectModeEnum.exam.equals(project.getMode())) {
			result.setExamScore(answerView.getExamScore());
			result.setQuestionScore(answerView.getExamInfo().getQuestionScore());
			// 计算错题
			AnswerExamInfo answerExamInfo = answerView.getExamInfo();
			LinkedHashMap<String, Double> questionScore = answerExamInfo.getQuestionScore();
			userBookService.saveWrongQuestion(questionScore);
		}
		// 白名单更新答题信息
		request.setId(answerView.getId());
		updateProjectPartnerByAnswer(request, project);
		// 完事儿删除 cookie 标识
		if (isNotBlank(answerIdFromCookie)) {
			Cookie cookie = new Cookie(randomSurveyCookieName, answerIdFromCookie);
			cookie.setMaxAge(0);
			ContextHelper.getCurrentHttpResponse().addCookie(cookie);
		}
		return result;
	}

	/**
	 * @param request
	 * @return
	 */
	@Override
	@SneakyThrows
	public PublicQueryVerifyView loadQuery(PublicQueryRequest request) {
		Tuple2<ProjectView, ProjectSetting.PublicQuery> projectAndQuery = getProjectAndQueryThenValidate(request);
		SurveySchema schema = buildQueryFormSchema(projectAndQuery.getFirst(), projectAndQuery.getSecond());
		PublicQueryVerifyView view = new PublicQueryVerifyView();
		view.setSchema(schema);
		return view;
	}

	@Override
	public PublicQueryView getQueryResult(PublicQueryRequest request) {
		Tuple2<ProjectView, ProjectSetting.PublicQuery> projectAndQuery = getProjectAndQueryThenValidate(request);
		SurveySchema schema = buildQueryResultSchema(projectAndQuery.getFirst(), projectAndQuery.getSecond());
		PublicQueryView view = new PublicQueryView();
		view.setSchema(schema);
		List<Answer> answers = findAnswerByQuery(request, projectAndQuery);
		// 根据配置的权限信息过滤答案
		LinkedHashMap<String, Integer> fieldPermission = projectAndQuery.getSecond().getFieldPermission();
		view.setFieldPermission(fieldPermission);
		view.setAnswers(answers.stream().map(answer -> {
			filterAnswerByFieldPermission(answer.getAnswer(), fieldPermission);
			PublicAnswerView answerView = new PublicAnswerView();
			answerView.setAnswerId(answer.getId());
			answerView.setAnswer(answer.getAnswer());
			if (FieldPermissionType.visible.equals(fieldPermission.get("examScore"))
					|| FieldPermissionType.editable.equals(fieldPermission.get("examScore"))) {
				// 公开查询允许查询分值
				answer.getAnswer().put("examScore", Collections.singletonMap("examScore", answer.getExamScore()));
			}
			answerView.setCreateAt(answer.getCreateAt());
			return answerView;
		}).collect(Collectors.toList()));
		return view;
	}

	@Override
	public List<PublicDictView> loadDict(PublicDictRequest request) {
		return dictItemService
				.list(Wrappers.<CommDictItem>lambdaQuery().eq(CommDictItem::getDictCode, request.getDictCode())
						.eq(request.getCascaderLevel() != null, CommDictItem::getItemLevel, request.getCascaderLevel())
						.eq(request.getParentValue() != null, CommDictItem::getParentItemValue,
								request.getParentValue())
						.and(isNotBlank(request.getSearch()),
								i -> i.like(CommDictItem::getItemName, request.getSearch()).or()
										.like(CommDictItem::getItemValue, request.getSearch()))
						.last(String.format("limit %d", request.getLimit() != null ? request.getLimit() : 50)))
				.stream().map(x -> {
					PublicDictView view = new PublicDictView();
					view.setLabel(x.getItemName());
					view.setValue(x.getItemValue());
					return view;
				}).collect(Collectors.toList());
	}

	/**
	 * 成绩查询页面获取成绩信息
	 * @param request
	 * @return
	 */
	@Override
	public PublicExamResult loadExamResult(PublicExamRequest request) {
		ProjectView project = projectService.getProject(request.getId());
		AnswerQuery answerQuery = new AnswerQuery();
		answerQuery.setId(request.getAnswerId());
		if (Boolean.TRUE.equals(project.getSetting().getSubmittedSetting().getRankVisible())) {
			answerQuery.setRankEnabled(true);
		}
		AnswerView answerView = answerService.getAnswer(answerQuery);
		ProjectSetting.SubmittedSetting submittedSetting = project.getSetting().getSubmittedSetting();
		ProjectSetting.ExamSetting examSetting = project.getSetting().getExamSetting();
		PublicExamResult result = new PublicExamResult();
		result.setName(project.getName());
		// 可以查看正确答案和解析;
		// 考试结束之后才可以查看正确答案和解析
		if (Boolean.TRUE.equals(submittedSetting.getAnswerAnalysis()) && examFinished(examSetting)) {
			result.setAnswer(answerView.getAnswer());
			result.setSchema(project.getSurvey());
			result.setExamInfo(answerView.getExamInfo());
		}
		// 显示成绩单
		if (Boolean.TRUE.equals(submittedSetting.getTranscriptVisible())) {
			result.setExamScore(answerView.getExamScore());
		}
		// 显示排名 TODO:显示排行榜
		if (Boolean.TRUE.equals(submittedSetting.getRankVisible())) {
			result.setRank(answerView.getRank());
		}
		// 随机问题
		if (answerView.getSurvey() != null) {
			result.setSchema(answerView.getSurvey());
		}
		result.setMetaInfo(answerView.getMetaInfo());
		return result;
	}

	@Override
	public void tempSaveAnswer(AnswerRequest request) {
		String projectId = request.getProjectId();
		if (!Integer.valueOf(0).equals(request.getTempSave()) || request.getTempAnswer() == null || projectId == null) {
			return;
		}
		String answerId = ContextHelper.getCookie(AppConsts.COOKIE_RANDOM_PROJECT_PREFIX + projectId);
		if (answerId == null) {
			return;
		}
		if (!SecurityContextUtils.isAuthenticated()) {
			// 目前仅支持登录用户后端暂存
			return;
		}
		AnswerRequest answerRequest = new AnswerRequest();
		answerRequest.setId(answerId);
		answerRequest.setTempSave(0);
		answerRequest.setTempAnswer(request.getTempAnswer());
		answerService.updateAnswer(answerRequest);
	}

	@Override
	public PublicLinkResult loadLinkResult(PublicLinkRequest request) {
		PublicLinkResult result = new PublicLinkResult();
		ProjectView projectView = projectService.getProject(request.getProjectId());
		if (projectView == null) {
			throw new ErrorCodeException(ErrorCode.ProjectNotFound);
		}
		SurveySchema currentQuestionSchema = SchemaHelper.flatSurveySchema(projectView.getSurvey()).stream()
				.filter(x -> x.getId().equals(request.getQuestionId())).findFirst().get();

		List<SurveySchema.LinkSurvey> linkSurveys = currentQuestionSchema.getChildren().stream()
				.filter(x -> request.getOptionId().equals(x.getId())).findFirst().get().getLinkSurveys();
		if (linkSurveys == null) {
			throw new ErrorCodeException(ErrorCode.LinkConditionNotFound);
		}
		LinkedHashMap<String, Map<String, Object>> fillAnswer = new LinkedHashMap<>();
		result.setAnswer(fillAnswer);
		for (SurveySchema.LinkSurvey linkSurvey : linkSurveys) {
			Answer answer = answerService
					.getOne(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, linkSurvey.getLinkSurveyId())
							.like(Answer::getAnswer, buildLinkLikeCondition(linkSurvey, request.getValue()))
							.orderByDesc(Answer::getCreateAt).last("limit 1"));
			fillLinkFieldAndAnswer(answer != null ? answer.getAnswer() : null, linkSurvey.getLinkFields(), fillAnswer);
		}

		return result;
	}

	@SneakyThrows
	private String buildLinkLikeCondition(SurveySchema.LinkSurvey linkSurvey, Object value) {
		Map<String, Object> optionValue = new HashMap<>();
		optionValue.put(linkSurvey.getLinkOptionId(), value);
		// Map<String, Map<String, Object>> questionValue = new HashMap<>();
		// questionValue.put(linkSurvey.getLinkQuestionId(), optionValue);
		return StringUtils.substringBetween(objectMapper.writeValueAsString(optionValue), "{", "}");
	}

	public void fillLinkFieldAndAnswer(LinkedHashMap answer, List<SurveySchema.LinkField> linkFields,
			LinkedHashMap<String, Map<String, Object>> fillAnswer) {
		for (SurveySchema.LinkField linkField : linkFields) {
			Map<String, Object> questionValueMap = fillAnswer.computeIfAbsent(linkField.getFillQuestionId(),
					(k) -> new HashMap<>());
			if (answer == null) {
				continue;
			}
			Map<String, Object> linkQuestionValue = (Map<String, Object>) answer.get(linkField.getLinkQuestionId());
			if (linkQuestionValue == null) {
				continue;
			}
			questionValueMap.put(linkField.getFillOptionId(), linkQuestionValue.get(linkField.getLinkOptionId()));
		}
	}

	/**
	 * 判断考试是否结束，未设置默认为已结束
	 * @param examSetting
	 * @return
	 */
	private boolean examFinished(ProjectSetting.ExamSetting examSetting) {
		if (examSetting.getEndTime() == null || examSetting.getEndTime() < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * 根据问卷设置校验问卷
	 * @param project
	 * @return
	 */
	private ProjectSetting validateProject(ProjectView project) {
		if (project == null) {
			throw new ErrorCodeException(ErrorCode.ProjectNotFound);
		}

		ProjectSetting setting = project.getSetting();
		String projectId = project.getId();
		if (setting.getStatus() == 0) {
			throw new ErrorCodeException(ErrorCode.SurveySuspend);
		}

		Long maxAnswers = setting.getAnswerSetting().getMaxAnswers();
		// 校验最大答案条数限制
		if (maxAnswers != null) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(project.getId());
			long totalAnswers = answerService.count(answerQuery);
			if (totalAnswers >= maxAnswers) {
				throw new ErrorCodeException(ErrorCode.ExceededMaxAnswers);
			}
			// 不将设置暴露给前端接口，不使用 @JsonProperty，否则设置页面回获取不到该值
			setting.getAnswerSetting().setMaxAnswers(null);
		}
		// 校验问卷是否已到结束时间
		Long endTime = setting.getAnswerSetting().getEndTime();
		if (endTime != null) {
			if (new Date().getTime() > endTime) {
				throw new ErrorCodeException(ErrorCode.ExceededEndTime);
			}
		}
		// 如果需要登录，则使用账号进行限制
		if (setting.getAnswerSetting().getLoginLimit() != null
				&& Boolean.TRUE.equals(setting.getAnswerSetting().getLoginRequired())) {
			validateLoginLimit(projectId, setting);
		}
		// cookie 限制
		if (setting.getAnswerSetting().getCookieLimit() != null) {
			validateCookieLimit(projectId, setting);
		}
		// ip 限制
		if (setting.getAnswerSetting().getIpLimit() != null) {
			validateIpLimit(projectId, setting);
		}
		// 白名单限制
		if (setting.getAnswerSetting().getWhitelistLimit() != null) {
			validateWhitelistLimit(projectId, setting);
		}
		validateExamSetting(project);
		return setting;
	}

	/**
	 * 答案校验
	 * @param project
	 * @param request
	 */
	private void validateAnswer(ProjectView project, AnswerRequest request) {
		List<SurveySchema> uniqueSchemaList = SchemaHelper.findSchemaListByAttribute(project.getSurvey(), "unique",
				true);
		SchemaHelper.TreeNode treeNode = SchemaHelper.SurveySchema2TreeNode(project.getSurvey());
		uniqueSchemaList.forEach(optionSchema -> {
			// 支持数值和字符串
			String questionId = treeNode.getTreeNodeMap().get(optionSchema.getId()).getParent().getData().getId();
			Object questionValue = request.getAnswer().get(questionId);
			if (questionValue == null) {
				return;
			}
			String uniqueQuery = String.format("\"%s\":", optionSchema.getId());
			if (SurveySchema.DataType.number == optionSchema.getAttribute().getDataType()) {
				uniqueQuery += ((Map) questionValue).get(optionSchema.getId());
			}
			else {
				uniqueQuery += "\"" + ((Map) questionValue).get(optionSchema.getId()) + "\"";
			}
			AnswerQuery query = new AnswerQuery();
			query.setProjectId(project.getId());
			query.setValueQuery(uniqueQuery);
			if (answerService.count(query) > 0) {
				String uniqueText = optionSchema.getAttribute().getUniqueText();
				throw new ValidationException(isNotBlank(uniqueText) ? uniqueText : "问卷重复保存");
			}
		});

		// 选项配额校验
		List<SurveySchema> hasQuotaSchemaList = SchemaHelper.findSchemaHasAttribute(project.getSurvey(), "quota");
		if (hasQuotaSchemaList.size() > 0) {
			ProjectQuery query = new ProjectQuery();
			query.setId(request.getProjectId());
			PublicStatisticsView statisticsView = statProject(query);

			hasQuotaSchemaList.forEach(optionSchema -> {
				String questionId = treeNode.getTreeNodeMap().get(optionSchema.getId()).getParent().getData().getId();
				Object questionValue = request.getAnswer().get(questionId);
				if (questionValue == null) {
					return;
				}
				boolean optionNotChecked = ((Map) questionValue).get(optionSchema.getId()) == null;
				if (optionNotChecked) {
					return;
				}
				PublicStatisticsView.QuestionStatistics questionStatistics = statisticsView.getQuestionStatistics()
						.get(questionId);
				int optionSelectedCount = questionStatistics.getOptionStatistics().stream()
						.filter(x -> x.getOptionId().equals(optionSchema.getId())).findFirst()
						.orElse(new PublicStatisticsView.OptionStatistics()).getCount();
				Integer quota = optionSchema.getAttribute().getQuota();
				if (quota != null && optionSelectedCount + 1 > quota) {
					throw new ValidationException("选项数量超过限制，请重新选择");
				}
			});
		}

	}

	private void validateExamSetting(ProjectView project) {
		ProjectSetting.ExamSetting examSetting = project.getSetting().getExamSetting();
		if (examSetting == null || !ProjectModeEnum.exam.equals(project.getMode())) {
			return;
		}
		// 校验考试开始时间
		if (examSetting.getStartTime() != null && new Date(examSetting.getStartTime()).compareTo(new Date()) > 0) {
			throw new ErrorCodeException(ErrorCode.ExamUnStarted);
		}
		// 校验考试结束时间
		if (examSetting.getEndTime() != null && new Date(examSetting.getEndTime()).compareTo(new Date()) < 0) {
			throw new ErrorCodeException(ErrorCode.ExamFinished);
		}
	}

	/**
	 * 校验问卷并且判断是否要更新最近一次的答案
	 * @param project
	 * @param request
	 * @return 最近一次的答案
	 */
	private AnswerView validateAndGetLatestAnswer(ProjectView project, AnswerRequest request) {
		ProjectSetting setting = project.getSetting();
		boolean needGetLatest = false;
		try {
			validateProject(project);
			validateAnswer(project, request);
			// 未设时间限制&需要登录&可以修改，永远修改的是同一份
			if (SecurityContextUtils.isAuthenticated() && setting != null
					&& Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
				needGetLatest = true;
			}
		}
		catch (ErrorCodeException e) {
			// 如果设置了时间限制，只能修改某个时间区间内的问卷
			// 登录&问卷已提交&允许修改，则可以继续修改
			if (ErrorCode.SurveySubmitted.equals(e.getErrorCode()) && SecurityContextUtils.isAuthenticated()
					&& setting != null && Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
				needGetLatest = true;
			}
			else {
				throw e;
			}
		}
		// 获取最近一份的问卷执行答案更新操作
		if (needGetLatest) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(project.getId());
			answerQuery.setLatest(true);
			AnswerView latestAnswer = answerService.getAnswer(answerQuery);
			if (latestAnswer != null) {
				return latestAnswer;
			}
		}
		return null;
	}

	/**
	 * 获取最近一次的答案
	 * @param projectView
	 * @return
	 */
	private LinkedHashMap<String, Object> getLatestAnswer(PublicProjectView projectView, String whitelistName) {
		// 打开了答案允许修改开关
		ProjectSetting projectSetting = projectView.getSetting();
		if (projectSetting == null || projectSetting.getSubmittedSetting() == null
				|| !Boolean.TRUE.equals(projectSetting.getSubmittedSetting().getEnableUpdate())) {
			return null;
		}
		AnswerQuery answerQuery = new AnswerQuery();
		answerQuery.setProjectId(projectView.getId());
		answerQuery.setLatest(true);
		if (whitelistName != null) {
			ProjectPartner partner = projectPartnerMapper.selectOne(
					Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, projectView.getId())
							.eq(ProjectPartner::getUserName, whitelistName));
			if (partner != null && !SecurityContextUtils.isAuthenticated()) {
				answerQuery.setCreateBy(partner.getId());
			}
		}
		// 通过白名单或者答卷人来获取最近一次的答卷记录
		if (SecurityContextUtils.isAuthenticated() || answerQuery.getCreateBy() != null) {
			return Optional.ofNullable(answerService.getAnswer(answerQuery)).map(x -> x.getAnswer()).orElse(null);
		}
		return null;
	}

	/**
	 * 公开查询修改答案，因为涉及到权限操作，需要将之前的答案和更新的答案做一个 merge 操作
	 * @param project
	 * @param answer
	 */
	private void validateAndMergeAnswer(ProjectView project, AnswerRequest answer) {
		if (isBlank(answer.getQueryId()) || isBlank(answer.getId())) {
			throw new ErrorCodeException(ErrorCode.QueryResultUpdateError);
		}
		try {
			// 公开查询设置必须存在，并且包含可编辑字段
			ProjectSetting.PublicQuery query = project.getSetting().getSubmittedSetting().getPublicQuery().stream()
					.filter(x -> x.getId().equals(answer.getQueryId())).findFirst().get();
			if (!query.getFieldPermission().values().contains(FieldPermissionType.editable)) {
				throw new ErrorCodeException(ErrorCode.QueryResultUpdateError);
			}
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setId(answer.getId());
			AnswerView latestAnswer = answerService.getAnswer(answerQuery);
			LinkedHashMap<String, Object> existAnswer = latestAnswer.getAnswer();
			existAnswer.forEach((key, value) -> {
				if (!answer.getAnswer().containsKey(key)) {
					answer.getAnswer().put(key, value);
				}
			});
		}
		catch (Exception e) {
			throw new ErrorCodeException(ErrorCode.QueryResultUpdateError);
		}
	}

	private void validateLoginLimit(String projectId, ProjectSetting setting) {
		String userId = SecurityContextUtils.getUserId();
		if (userId == null) {
			log.info("user is empty");
			return;
		}
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		query.setCreateBy(userId);
		doValidate(setting, query, setting.getAnswerSetting().getLoginLimit());
	}

	private void validateCookieLimit(String projectId, ProjectSetting setting) {
		HttpServletRequest request = ContextHelper.getCurrentHttpRequest();

		Cookie limitCookie = WebUtils.getCookie(request, AppConsts.COOKIE_LIMIT_NAME);
		if (limitCookie == null) {
			// 添加 cookie
			HttpServletResponse response = ContextHelper.getCurrentHttpResponse();

			final Cookie cookie = new Cookie(AppConsts.COOKIE_LIMIT_NAME, UUID.randomUUID().toString());
			cookie.setPath("/");
			cookie.setMaxAge(100 * 360 * 24 * 60 * 60);
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			response.addCookie(cookie);
			return;
		}
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		query.setCookie(limitCookie.getValue());
		doValidate(setting, query, setting.getAnswerSetting().getCookieLimit());
	}

	private void validateIpLimit(String projectId, ProjectSetting setting) {
		HttpServletRequest request = ContextHelper.getCurrentHttpRequest();
		String ip = IPUtils.getClientIpAddress(request);
		if (ip == null) {
			log.info("ip is empty");
			return;
		}
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		query.setIp(ip);
		doValidate(setting, query, setting.getAnswerSetting().getIpLimit());
	}

	private void validateWhitelistLimit(String projectId, ProjectSetting setting) {
		AnswerQuery query = new AnswerQuery();
		query.setProjectId(projectId);
		// 如果白名单校验成功，导入用户获取partner表的id，系统用户是当前登录用户 id
		String createBy = (String) ContextHelper.getCurrentHttpRequest().getAttribute("createBy");
		if (createBy != null) {
			query.setCreateBy(createBy);
			doValidate(setting, query, setting.getAnswerSetting().getWhitelistLimit());
		}
	}

	private void doValidate(ProjectSetting setting, AnswerQuery query, ProjectSetting.UniqueLimitSetting limitSetting) {
		// 通过 cron 计算时间窗
		CronHelper helper = new CronHelper(limitSetting.getLimitFreq().getCron());
		Tuple2<LocalDateTime, LocalDateTime> currentWindow = helper.currentWindow();
		if (currentWindow != null) {
			query.setStartTime(Date.from(currentWindow.getFirst().atZone(ZoneId.systemDefault()).toInstant()));
			query.setEndTime(Date.from(currentWindow.getSecond().atZone(ZoneId.systemDefault()).toInstant()));
		}
		long total = answerService.count(query);
		// 允许修改答案的话就获取最近一次的答案，不抛出异常
		if (limitSetting.getLimitNum() != null && total >= limitSetting.getLimitNum()
				&& (setting.getSubmittedSetting() == null
						|| !Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate()))) {
			throw new ErrorCodeException(ErrorCode.SurveySubmitted);
		}
	}

	private Tuple2<ProjectView, ProjectSetting.PublicQuery> getProjectAndQueryThenValidate(PublicQueryRequest request) {
		ProjectView project = projectService.getProject(request.getId());
		List<ProjectSetting.PublicQuery> queries = project.getSetting().getSubmittedSetting().getPublicQuery();
		if (queries == null || queries.size() == 0) {
			throw new ErrorCodeException(ErrorCode.QueryNotExist);
		}
		ProjectSetting.PublicQuery query = queries.stream().filter(x -> x.getId().equals(request.getResultId()))
				.findFirst().orElseThrow(() -> new ErrorCodeException(ErrorCode.QueryNotExist));
		validatePublicQuery(query, request.getAnswer());
		return new Tuple2<>(project, query);
	}

	/**
	 * @param query 公开查询配置
	 * @param answer 查询答案
	 */
	@SneakyThrows
	private void validatePublicQuery(ProjectSetting.PublicQuery query, LinkedHashMap answer) {
		if (Boolean.FALSE.equals(query.getEnabled())) {
			throw new ErrorCodeException(ErrorCode.QueryDisabled);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> linkValidityPeriod = query.getLinkValidityPeriod();
		if (linkValidityPeriod != null && linkValidityPeriod.size() == 2 && !DateUtils.isBetween(new Date(),
				sdf.parse(linkValidityPeriod.get(0)), sdf.parse(linkValidityPeriod.get(1)))) {
			throw new ErrorCodeException(ErrorCode.QueryDisabled);
		}
		// 校验密码
		if (isNotBlank(query.getPassword()) && answer != null) {
			if (!answer.containsKey(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID)) {
				throw new ErrorCodeException(ErrorCode.QueryPasswordError);
			}
			String password = (String) ((Map) answer.get(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID))
					.get(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID);
			if (isBlank(password) || !query.getPassword().equals(password.trim())) {
				throw new ErrorCodeException(ErrorCode.QueryPasswordError);
			}
			answer.remove(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID);
		}
	}

	/**
	 * 前端支持动态主题(问卷主题/表单主题)切换，动态构建查询表单
	 * @param project
	 * @param query
	 * @return
	 */
	private SurveySchema buildQueryFormSchema(ProjectView project, ProjectSetting.PublicQuery query) {
		SurveySchema schema = SurveySchema.builder().id(query.getId()).title(query.getTitle())
				.description(query.getDescription())
				.children(findMatchChildrenInSchema(query.getConditionQuestion(), project))
				.attribute(SurveySchema.Attribute.builder().submitButton("查询").build()).build();
		// 目前只支持文本题 #{huaw}#{fhpd}
		if (isNotBlank(query.getPassword())) {
			// 添加一个password的schema，用于密码校验
			SurveySchema passwordSchema = SurveySchema.builder().id(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID)
					.title("密码").type(SurveySchema.QuestionType.FillBlank)
					.attribute(SurveySchema.Attribute.builder().required(true).build()).build();
			passwordSchema.setChildren(Collections
					.singletonList(SurveySchema.builder().id(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID).build()));
			schema.getChildren().add(passwordSchema);
		}
		return schema;
	}

	private List<SurveySchema> findMatchChildrenInSchema(String conditionQuestion, ProjectView project) {
		if (isBlank(conditionQuestion)) {
			return new ArrayList<>();
		}
		Pattern condPattern = Pattern.compile("#\\{(.*?)\\}");
		Matcher m = condPattern.matcher(conditionQuestion);
		List<String> conditionIds = new ArrayList<>();
		while (m.find()) {
			String qId = m.group(1);
			conditionIds.add(qId);
		}
		return SchemaHelper.flatSurveySchema(project.getSurvey()).stream()
				.filter(qSchema -> conditionIds.contains(qSchema.getId())).collect(Collectors.toList());
	}

	/**
	 * 根据配置的字段权限信息来过滤要查询的字段
	 * @param project
	 * @param query
	 * @return
	 */
	private SurveySchema buildQueryResultSchema(ProjectView project, ProjectSetting.PublicQuery query) {
		SurveySchema schema = project.getSurvey().deepCopy();
		SchemaHelper.updateSchemaByPermission(query.getFieldPermission(), schema);
		if (query.getFieldPermission().values().contains(FieldPermissionType.editable)) {
			schema.setAttribute(SurveySchema.Attribute.builder().submitButton("修改").suffix(null).build());
		}
		else {
			schema.setAttribute(null);
		}
		if (ProjectModeEnum.exam.equals(project.getMode())) {
			schema.getChildren()
					.add(SurveySchema.builder().id("examScore").title("分数").type(SurveySchema.QuestionType.FillBlank)
							.attribute(SurveySchema.Attribute.builder().readOnly(true).build())
							.children(Collections.singletonList(SurveySchema.builder().id("examScore").build()))
							.build());
		}
		// 公开查询表单需要取消结束、显示隐藏、跳转规则
		SchemaHelper.ignoreAttributes(schema, "finishRule", "visibleRule", "jumpRule");
		return schema;
	}

	/**
	 * @param request 提交的请求
	 * @param projectAndQuery 项目信息和当前查询信息
	 * @return
	 */
	private List<Answer> findAnswerByQuery(PublicQueryRequest request,
			Tuple2<ProjectView, ProjectSetting.PublicQuery> projectAndQuery) {
		ProjectView projectView = projectAndQuery.getFirst();
		List<SurveySchema> conditionSchemaList = findMatchChildrenInSchema(
				projectAndQuery.getSecond().getConditionQuestion(), projectAndQuery.getFirst());
		if (conditionSchemaList.size() == 0 && request.getQuery().size() == 0) {
			throw new ErrorCodeException(ErrorCode.QueryResultNotExist);
		}
		SchemaHelper.TreeNode treeNode = SchemaHelper.SurveySchema2TreeNode(projectView.getSurvey());

		// 通过 url 参数构建查询表单
		LinkedHashMap<String, Map> queryFormValues = buildFormValuesFromQueryParameter(treeNode, request.getQuery());
		// 将查询表单和url参数构建的查询表单合并
		queryFormValues.putAll(request.getAnswer());

		List<Answer> answer = ((AnswerServiceImpl) answerService)
				.list(Wrappers.<Answer>lambdaQuery().eq(Answer::getProjectId, projectView.getId()).and(i -> {
					queryFormValues.forEach((qId, qValueObj) -> {
						i.like(Answer::getAnswer,
								buildLikeQueryConditionOfQuestion(treeNode.getTreeNodeMap().get(qId), qValueObj));
					});
				}));
		if (answer.size() == 0) {
			throw new ErrorCodeException(ErrorCode.QueryResultNotExist);
		}
		// 根据配置过滤结果
		return answer;
	}

	/**
	 * 通过查询参数里面构建 form values
	 * @param query
	 * @return
	 */
	private LinkedHashMap buildFormValuesFromQueryParameter(SchemaHelper.TreeNode surveySchemaTreeNode,
			Map<String, String> query) {
		LinkedHashMap<String, Map> formValues = new LinkedHashMap<>();
		query.forEach((id, value) -> {
			// 默认为选项
			SchemaHelper.TreeNode findNode = surveySchemaTreeNode.getTreeNodeMap().get(id);
			String questionId = findNode.getParent().getData().getId();
			Map questionValueMap = formValues.computeIfAbsent(questionId, k -> new HashMap<>());
			questionValueMap.put(id, value);
		});
		return formValues;
	}

	/**
	 * 通过问题答案手动构建like 查询
	 * @param qNode 当前问题的 schema node 节点
	 * @param qValueObj 当前问题的答案
	 * @return
	 */
	private String buildLikeQueryConditionOfQuestion(SchemaHelper.TreeNode qNode, Map qValueObj) {
		SurveySchema optionSchema = qNode.getData().getChildren().get(0);
		String optionId = optionSchema.getId();
		Object optionValue = qValueObj.get(optionId);
		String value = optionValue.toString();
		// 选项非数值类型
		if (optionSchema.getAttribute() == null
				|| !SurveySchema.DataType.number.equals(optionSchema.getAttribute().getDataType())) {
			value = "\"" + value + "\"";
		}
		return String.format("{\"%s\":%s}", optionId, value);
	}

	/**
	 * 根据字段权限配置过滤结果集，过滤掉隐藏题的答案
	 * @param answer
	 * @param fieldPermission
	 */
	private void filterAnswerByFieldPermission(LinkedHashMap answer, LinkedHashMap<String, Integer> fieldPermission) {
		fieldPermission.entrySet().forEach(entry -> {
			String qId = entry.getKey();
			Integer permission = entry.getValue();
			if (FieldPermissionType.hidden == permission) {
				answer.remove(qId);
			}
		});
	}

	/**
	 * 如果问卷需要登录、需要密码答卷、需要白名单答卷进入问卷之前需要弹出验证表单
	 * @param project 当前项目
	 * @param answer 查询表单的答案
	 * @return 返回查询表单的 schema，如果未空，则会直接进入到答卷页面
	 */
	private SurveySchema convertAndValidateLoginFormIfNeeded(ProjectView project,
			LinkedHashMap<String, Object> answer) {
		boolean loginRequired = false;
		// 需要更新答题者为已访问的状态
		boolean updatePartnerVisited = false;
		LambdaQueryWrapper<ProjectPartner> projectPartnerQuery = Wrappers.<ProjectPartner>lambdaQuery()
				.eq(ProjectPartner::getProjectId, project.getId());
		Authentication authentication = null;
		List<SurveySchema> queryConditions = new ArrayList<>();
		SurveySchema loginFormSchema = SurveySchema.builder().id(project.getId()).children(queryConditions)
				.title(project.getName()).build();
		// 错题练习模式需要登录
		if (project != null && project.getSetting() != null && project.getSetting().getExamSetting() != null) {
			ProjectSetting.ExamSetting examSetting = project.getSetting().getExamSetting();
			if (Boolean.TRUE.equals(examSetting.getRandomSurveyWrong()) && !SecurityContextUtils.isAuthenticated()) {
				loginRequired = true;
				SchemaHelper.appendChildIfNotExist(loginFormSchema,
						SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.username));
				SchemaHelper.appendChildIfNotExist(loginFormSchema,
						SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.password));

				if (answer != null) {
					authentication = validateUsernameAndPassword(answer);
				}
			}
		}
		if (project != null && project.getSetting() != null && project.getSetting().getAnswerSetting() != null) {
			ProjectSetting.AnswerSetting answerSetting = project.getSetting().getAnswerSetting();
			// 需要登录答卷
			if (Boolean.TRUE.equals(answerSetting.getLoginRequired()) && !SecurityContextUtils.isAuthenticated()) {
				loginRequired = true;
				SchemaHelper.appendChildIfNotExist(loginFormSchema,
						SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.username));
				SchemaHelper.appendChildIfNotExist(loginFormSchema,
						SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.password));

				if (answer != null) {
					authentication = validateUsernameAndPassword(answer);
				}
			}
			// 需要密码答卷
			if (answerSetting.getPassword() != null) {
				loginRequired = true;
				queryConditions
						.add(SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.extraPassword));

				if (answer != null) {
					if (!answerSetting.getPassword().equals(
							SchemaHelper.getLoginFormAnswer(answer, SchemaHelper.LoginFormFieldEnum.extraPassword))) {
						throw new ErrorCodeException(ErrorCode.ValidationError);
					}
				}
			}
			// 白名单为系统用户
			if (answerSetting.getWhitelistType() != null
					&& ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType() == answerSetting.getWhitelistType()) {
				// 如果当前已登录且当前用户不在白名单内，则报错
				if (SecurityContextUtils.isAuthenticated()) {
					boolean currentHasPerm = projectPartnerMapper.selectCount(
							Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, project.getId())
									.eq(ProjectPartner::getType, ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType())
									.eq(ProjectPartner::getUserId, SecurityContextUtils.getUserId())) == 1;
					if (!currentHasPerm) {
						throw new ErrorCodeException(ErrorCode.PermVerifyFailed);
					}
					updatePartnerVisited = true;
					projectPartnerQuery.eq(ProjectPartner::getUserId, SecurityContextUtils.getUserId())
							.eq(ProjectPartner::getType, ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType());
				}
				else {
					// 需要执行登录操作
					SchemaHelper.appendChildIfNotExist(loginFormSchema,
							SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.username));
					SchemaHelper.appendChildIfNotExist(loginFormSchema,
							SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.password));
					loginRequired = true;

					if (answer != null) {
						if (authentication == null) {
							authentication = validateUsernameAndPassword(answer);
						}
						// 执行登录操作
						UserInfo user = (UserInfo) authentication.getPrincipal();
						// 判断登录用户是否在白名单里面
						boolean currentHasPerm = projectPartnerMapper.selectCount(
								Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, project.getId())
										.eq(ProjectPartner::getUserId, user.getUserId())) == 1;
						if (!currentHasPerm) {
							throw new ErrorCodeException(ErrorCode.PermVerifyFailed);
						}

						updatePartnerVisited = true;
						projectPartnerQuery.eq(ProjectPartner::getUserId, user.getUserId()).eq(ProjectPartner::getType,
								ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType());
					}
				}
			}
			// 白名单为导入用户
			if (answerSetting.getWhitelistType() != null
					&& ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == answerSetting.getWhitelistType()) {
				SchemaHelper.appendChildIfNotExist(loginFormSchema,
						SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.whitelistName));
				loginRequired = true;

				if (answer != null) {
					// 校验登录白名单名称是否在白名单列表里面
					String whitelistName = (String) SchemaHelper.getLoginFormAnswer(answer,
							SchemaHelper.LoginFormFieldEnum.whitelistName);
					if (whitelistName == null) {
						throw new ErrorCodeException(ErrorCode.PermVerifyFailed);
					}
					boolean currentHasPerm = projectPartnerMapper.selectCount(
							Wrappers.<ProjectPartner>lambdaQuery().eq(ProjectPartner::getProjectId, project.getId())
									.eq(ProjectPartner::getUserName, whitelistName)) == 1;
					if (!currentHasPerm) {
						throw new ErrorCodeException(ErrorCode.PermVerifyFailed);
					}

					updatePartnerVisited = true;
					projectPartnerQuery.eq(ProjectPartner::getUserName, whitelistName).eq(ProjectPartner::getType,
							ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType());
				}
			}

		}

		if (authentication != null) {
			// 执行登录操作，方便后续保存答案的时候获取认证信息
			UserInfo user = (UserInfo) authentication.getPrincipal();
			HttpCookie cookie = ResponseCookie
					.from(AppConsts.TOKEN_NAME, jwtTokenUtil.generateAccessToken(new UserTokenView(user.getUserId())))
					.path("/").httpOnly(true).build();
			ContextHelper.getCurrentHttpResponse().setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		}

		// 更新问卷状态为已访问
		if (updatePartnerVisited) {
			ProjectPartner projectPartner = projectPartnerMapper.selectOne(projectPartnerQuery);
			if (projectPartner != null && projectPartner.getStatus() == AppConsts.ProjectPartnerStatus.UNVISITED) {
				projectPartner.setStatus(AppConsts.ProjectPartnerStatus.VISITED);
				projectPartnerMapper.updateById(projectPartner);
			}
			// 如果配置的外部用户，则 createBy 为 partner 的 id，如果是内部用户则是用户 id
			if (projectPartner.getUserName() != null) {
				ContextHelper.getCurrentHttpRequest().setAttribute("createBy", projectPartner.getId());
			}
			else if (projectPartner.getUserId() != null) {
				ContextHelper.getCurrentHttpRequest().setAttribute("createBy", SecurityContextUtils.getUserId());
			}
		}

		if (loginRequired) {
			return loginFormSchema;
		}

		return null;
	}

	/**
	 * 答卷前登录校验用户名和密码
	 * @param answer
	 * @return
	 */
	private Authentication validateUsernameAndPassword(LinkedHashMap<String, Object> answer) {
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					SchemaHelper.getLoginFormAnswer(answer, SchemaHelper.LoginFormFieldEnum.username),
					SchemaHelper.getLoginFormAnswer(answer, SchemaHelper.LoginFormFieldEnum.password)));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return authentication;
		}
		catch (Exception e) {
			throw new ErrorCodeException(ErrorCode.ValidationError);
		}
	}

	/**
	 * 如果存在白名单，答卷完更新白名单为已访问的状态，由于导入用户白名单没有系统用户id，需要将答案的创建人更新为答题参与表的id。
	 * @param request
	 * @param project
	 */
	private void updateProjectPartnerByAnswer(AnswerRequest request, ProjectView project) {
		if (project.getSetting() != null && project.getSetting().getAnswerSetting() != null) {
			Integer whitelistType = project.getSetting().getAnswerSetting().getWhitelistType();
			if (whitelistType == null) {
				return;
			}

			LambdaQueryWrapper<ProjectPartner> queryWrapper = Wrappers.<ProjectPartner>lambdaQuery()
					.eq(ProjectPartner::getProjectId, project.getId());
			if (ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType() == whitelistType) {
				queryWrapper.eq(ProjectPartner::getUserId, SecurityContextUtils.getUserId()).eq(ProjectPartner::getType,
						ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType());
				if (!SecurityContextUtils.isAuthenticated()) {
					return;
				}
			}
			else if (ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == whitelistType) {
				queryWrapper.eq(ProjectPartner::getUserName, request.getWhitelistName()).eq(ProjectPartner::getType,
						ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType());
			}

			ProjectPartner projectPartner = projectPartnerMapper.selectOne(queryWrapper);
			projectPartner.setStatus(AppConsts.ProjectPartnerStatus.ANSWERED);
			projectPartnerMapper.updateById(projectPartner);

			// 白名单导入用户答题需要更新答案表的 createBy 为 partner 的 id
			if (ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == whitelistType) {
				AnswerRequest answerUpdateRequest = new AnswerRequest();
				answerUpdateRequest.setId(request.getId());
				answerUpdateRequest.setCreateBy(projectPartner.getId());
				answerService.updateAnswer(answerUpdateRequest);
			}

		}
	}

	/**
	 * 随机问题，分为随机问题和错题练习
	 * @param project
	 */
	private void replaceSchemaIfRandomSchema(ProjectView project, PublicProjectView projectView) {
		if (ProjectModeEnum.exam != project.getMode()) {
			return;
		}
		SurveySchema source = project.getSurvey();
		Boolean randomSurveyWrong = project.getSetting().getExamSetting().getRandomSurveyWrong();
		if (Boolean.TRUE.equals(randomSurveyWrong)) {
			if (!SecurityContextUtils.isAuthenticated()) {
				// 需要登录
			}
			List<UserBook> wrongUserQuestions = userBookService
					.list(Wrappers.<UserBook>lambdaQuery().eq(UserBook::getType, UserBookServiceImpl.BOOK_TYPE_WRONG)
							.eq(UserBook::getCreateBy, SecurityContextUtils.getUserId()));
			if (wrongUserQuestions.size() == 0) {
				return;
			}
			SurveySchema randomSchema = SurveySchema.builder().id(source.getId()).children(templateService
					.listByIds(wrongUserQuestions.stream().map(x -> x.getTemplateId()).collect(Collectors.toList()))
					.stream().map(x -> {
						SurveySchema schema = x.getTemplate();
						schema.setId(x.getId());
						schema.setTitle(x.getName());
						return schema;
					}).collect(Collectors.toList())).title(source.getTitle()).attribute(source.getAttribute())
					.description(source.getDescription()).build();
			projectView.setSurvey(randomSchema);
			return;
		}
		List<ProjectSetting.RandomSurveyCondition> randomSurveyCondition = project.getSetting().getExamSetting()
				.getRandomSurvey();
		if (randomSurveyCondition == null || randomSurveyCondition.size() == 0) {
			return;
		}
		// 从已有答案里面加载 schema
		String cookieName = AppConsts.COOKIE_RANDOM_PROJECT_PREFIX + project.getId();
		String answerId = ContextHelper.getCookie(cookieName);
		if (answerId != null) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setId(answerId);
			AnswerView answerView = answerService.getAnswer(answerQuery);
			if (answerView != null && answerView.getSurvey() != null) {
				projectView.setSurvey(answerView.getSurvey());
				projectView.setTempAnswer(answerView.getTempAnswer());
				return;
			}
		}
		// 从题库里面挑题
		List<SurveySchema> questionSchemaList = repoService.pickQuestionFromRepo(randomSurveyCondition);
		// 最终的试卷=编辑器的问题+题库挑的题
		if (project.getSurvey() != null && !CollectionUtils.isEmpty(project.getSurvey().getChildren())) {
			questionSchemaList = Stream.concat(project.getSurvey().getChildren().stream(), questionSchemaList.stream())
					.collect(Collectors.toList());
		}

		SurveySchema randomSchema = SurveySchema.builder().id(source.getId()).children(questionSchemaList)
				.title(source.getTitle()).attribute(source.getAttribute()).description(source.getDescription()).build();
		if (questionSchemaList.size() > 0) {
			projectView.setSurvey(randomSchema);
			AnswerRequest answerRequest = new AnswerRequest();
			answerRequest.setSurvey(randomSchema);
			answerRequest.setProjectId(project.getId());
			AnswerView answerView = answerService.saveAnswer(answerRequest);
			Cookie cookie = new Cookie(cookieName, answerView.getId());
			cookie.setMaxAge(7 * 24 * 60 * 60);
			ContextHelper.getCurrentHttpResponse().addCookie(cookie);
		}
	}

}
