package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.Tuple2;
import cn.surveyking.server.core.constant.*;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.security.JwtTokenUtil;
import cn.surveyking.server.core.uitls.*;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.ProjectViewMapper;
import cn.surveyking.server.domain.model.Answer;
import cn.surveyking.server.domain.model.ProjectPartner;
import cn.surveyking.server.mapper.ProjectPartnerMapper;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.ProjectService;
import cn.surveyking.server.service.SurveyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

	private final AnswerService answerService;

	private final ProjectPartnerMapper projectPartnerMapper;

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	/**
	 * answerService ??????????????????????????????????????????????????????????????????????????? schema
	 * @param query
	 * @return
	 */
	@Override
	public PublicProjectView loadProject(ProjectQuery query) {
		ProjectView project = projectService.getProject(query.getId());
		// ?????????????????? schema
		SurveySchema loginFormSchema = convertAndValidateLoginFormIfNeeded(project, null);
		// ????????????
		validateProject(project);
		// ?????????????????????????????? schema ???????????????????????? schema
		PublicProjectView projectView = projectViewMapper.toPublicProjectView(project);
		if (loginFormSchema != null) {
			projectView.setSurvey(loginFormSchema);
			projectView.setLoginRequired(true);
		}
		else {
			// ??????????????????
			projectView.setAnswer(getLatestAnswer(projectView, null));
		}

		return projectView;
	}

	/**
	 * ????????????
	 * @param query
	 * @return
	 */
	@Override
	public PublicProjectView validateProject(ProjectQuery query) {
		String projectId = query.getId();
		ProjectView project = projectService.getProject(projectId);
		// ????????????
		convertAndValidateLoginFormIfNeeded(project, query.getAnswer());
		// ????????????
		validateProject(project);
		PublicProjectView projectView = projectViewMapper.toPublicProjectView(project);
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
	public PublicAnswerView saveAnswer(AnswerRequest request, HttpServletRequest httpRequest) {
		String projectId = request.getProjectId();

		PublicAnswerView result = new PublicAnswerView();
		ProjectView project = projectService.getProject(projectId);
		ProjectSetting setting = project.getSetting();

		String answerId = null;
		if (isNotBlank(request.getQueryId())) {
			// ????????????????????????
			answerId = request.getId();
			validateAndMergeAnswer(project, request);
		}
		else {
			// ???????????????????????? ??????????????????
			AnswerView latestAnswer = validateAndGetLatestAnswer(project);
			if (latestAnswer != null) {
				answerId = latestAnswer.getId();
			}
		}

		// ????????????
		request.setId(answerId);
		AnswerView answerView = answerService.saveAnswer(request, httpRequest);
		// ????????????????????????????????????????????????
		if (Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())
				&& !SecurityContextUtils.isAuthenticated()) {
			result.setAnswerId(answerView.getId());
		}
		// ???????????????????????????????????????
		if (ProjectModeEnum.exam.equals(project.getMode())) {
			result.setExamScore(answerView.getExamScore());
			result.setQuestionScore(answerView.getExamInfo().getQuestionScore());
		}
		// ???????????????????????????
		request.setId(answerView.getId());
		updateProjectPartnerByAnswer(request, project);
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
		// ???????????????????????????????????????
		LinkedHashMap<String, Integer> fieldPermission = projectAndQuery.getSecond().getFieldPermission();
		view.setFieldPermission(fieldPermission);
		view.setAnswers(answers.stream().map(answer -> {
			filterAnswerByFieldPermission(answer.getAnswer(), fieldPermission);
			PublicAnswerView answerView = new PublicAnswerView();
			answerView.setAnswerId(answer.getId());
			answerView.setAnswer(answer.getAnswer());
			if (FieldPermissionType.visible.equals(fieldPermission.get("examScore"))
					|| FieldPermissionType.editable.equals(fieldPermission.get("examScore"))) {
				// ??????????????????????????????
				answer.getAnswer().put("examScore", Collections.singletonMap("examScore", answer.getExamScore()));
			}
			answerView.setCreateAt(answer.getCreateAt());
			return answerView;
		}).collect(Collectors.toList()));
		return view;
	}

	/**
	 * ??????????????????????????????
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
		// ??????????????????????????????
		if (maxAnswers != null) {
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setProjectId(project.getId());
			long totalAnswers = answerService.count(answerQuery);
			if (totalAnswers >= maxAnswers) {
				throw new ErrorCodeException(ErrorCode.ExceededMaxAnswers);
			}
			// ????????????????????????????????????????????? @JsonProperty??????????????????????????????????????????
			setting.getAnswerSetting().setMaxAnswers(null);
		}
		// ????????????????????????????????????
		Long endTime = setting.getAnswerSetting().getEndTime();
		if (endTime != null) {
			if (new Date().getTime() > endTime) {
				throw new ErrorCodeException(ErrorCode.ExceededEndTime);
			}
		}
		// ????????????????????????????????????????????????
		if (setting.getAnswerSetting().getLoginLimit() != null
				&& Boolean.TRUE.equals(setting.getAnswerSetting().getLoginRequired())) {
			validateLoginLimit(projectId, setting);
		}
		// cookie ??????
		if (setting.getAnswerSetting().getCookieLimit() != null) {
			validateCookieLimit(projectId, setting);
		}
		// ip ??????
		if (setting.getAnswerSetting().getIpLimit() != null) {
			validateIpLimit(projectId, setting);
		}
		// ???????????????
		if (setting.getAnswerSetting().getWhitelistLimit() != null) {
			validateWhitelistLimit(projectId, setting);
		}
		validateExamSetting(project);
		return setting;
	}

	private void validateExamSetting(ProjectView project) {
		ProjectSetting.ExamSetting examSetting = project.getSetting().getExamSetting();
		if (examSetting == null || !ProjectModeEnum.exam.equals(project.getMode())) {
			return;
		}
		// ????????????????????????
		if (examSetting.getStartTime() != null && new Date(examSetting.getStartTime()).compareTo(new Date()) > 0) {
			throw new ErrorCodeException(ErrorCode.ExamUnStarted);
		}
		// ????????????????????????
		if (examSetting.getEndTime() != null && new Date(examSetting.getEndTime()).compareTo(new Date()) < 0) {
			throw new ErrorCodeException(ErrorCode.ExamFinished);
		}
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 * @param project
	 * @return ?????????????????????
	 */
	private AnswerView validateAndGetLatestAnswer(ProjectView project) {
		ProjectSetting setting = project.getSetting();
		boolean needGetLatest = false;
		try {
			validateProject(project);
			// ??????????????????&????????????&??????????????????????????????????????????
			if (SecurityContextUtils.isAuthenticated() && setting != null
					&& Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
				needGetLatest = true;
			}
		}
		catch (ErrorCodeException e) {
			// ????????????????????????????????????????????????????????????????????????
			// ??????&???????????????&????????????????????????????????????
			if (ErrorCode.SurveySubmitted.equals(e.getErrorCode()) && SecurityContextUtils.isAuthenticated()
					&& setting != null && Boolean.TRUE.equals(setting.getSubmittedSetting().getEnableUpdate())) {
				needGetLatest = true;
			}
			else {
				throw e;
			}
		}
		// ???????????????????????????????????????????????????
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
	 * ???????????????????????????
	 * @param projectView
	 * @return
	 */
	private LinkedHashMap<String, Object> getLatestAnswer(PublicProjectView projectView, String whitelistName) {
		// ???????????????????????????????????????
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
		// ??????????????????????????????????????????????????????????????????
		if (SecurityContextUtils.isAuthenticated() || answerQuery.getCreateBy() != null) {
			return Optional.ofNullable(answerService.getAnswer(answerQuery)).map(x -> x.getAnswer()).orElse(null);
		}
		return null;
	}

	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????? merge ??????
	 * @param project
	 * @param answer
	 */
	private void validateAndMergeAnswer(ProjectView project, AnswerRequest answer) {
		if (isBlank(answer.getQueryId()) || isBlank(answer.getId())) {
			throw new ErrorCodeException(ErrorCode.QueryResultUpdateError);
		}
		try {
			// ????????????????????????????????????????????????????????????
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
			// ?????? cookie
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
		// ????????????????????????????????????????????????partner??????id???????????????????????????????????? id
		String createBy = (String) ContextHelper.getCurrentHttpRequest().getAttribute("createBy");
		if (createBy != null) {
			query.setCreateBy(createBy);
			doValidate(setting, query, setting.getAnswerSetting().getWhitelistLimit());
		}
	}

	private void doValidate(ProjectSetting setting, AnswerQuery query, ProjectSetting.UniqueLimitSetting limitSetting) {
		// ?????? cron ???????????????
		CronHelper helper = new CronHelper(limitSetting.getLimitFreq().getCron());
		Tuple2<LocalDateTime, LocalDateTime> currentWindow = helper.currentWindow();
		if (currentWindow != null) {
			query.setStartTime(Date.from(currentWindow.getFirst().atZone(ZoneId.systemDefault()).toInstant()));
			query.setEndTime(Date.from(currentWindow.getSecond().atZone(ZoneId.systemDefault()).toInstant()));
		}
		long total = answerService.count(query);
		// ????????????????????????????????????????????????????????????????????????
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
	 * @param query ??????????????????
	 * @param answer ????????????
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
		// ????????????
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
	 * ????????????????????????(????????????/????????????)?????????????????????????????????
	 * @param project
	 * @param query
	 * @return
	 */
	private SurveySchema buildQueryFormSchema(ProjectView project, ProjectSetting.PublicQuery query) {
		SurveySchema schema = SurveySchema.builder().id(query.getId()).title(query.getTitle())
				.description(query.getDescription())
				.children(findMatchChildrenInSchema(query.getConditionQuestion(), project))
				.attribute(SurveySchema.Attribute.builder().submitButton("??????").build()).build();
		// ???????????????????????? #{huaw}#{fhpd}
		if (isNotBlank(query.getPassword())) {
			// ????????????password???schema?????????????????????
			SurveySchema passwordSchema = SurveySchema.builder().id(AppConsts.PUBLIC_QUERY_PASSWORD_FIELD_ID)
					.title("??????").type(SurveySchema.QuestionType.FillBlank)
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
	 * ????????????????????????????????????????????????????????????
	 * @param project
	 * @param query
	 * @return
	 */
	private SurveySchema buildQueryResultSchema(ProjectView project, ProjectSetting.PublicQuery query) {
		SurveySchema schema = project.getSurvey().deepCopy();
		SchemaHelper.updateSchemaByPermission(query.getFieldPermission(), schema);
		if (query.getFieldPermission().values().contains(FieldPermissionType.editable)) {
			schema.setAttribute(SurveySchema.Attribute.builder().submitButton("??????").suffix(null).build());
		}
		else {
			schema.setAttribute(null);
		}
		if (ProjectModeEnum.exam.equals(project.getMode())) {
			schema.getChildren()
					.add(SurveySchema.builder().id("examScore").title("??????").type(SurveySchema.QuestionType.FillBlank)
							.attribute(SurveySchema.Attribute.builder().readOnly(true).build())
							.children(Collections.singletonList(SurveySchema.builder().id("examScore").build()))
							.build());
		}
		return schema;
	}

	/**
	 * @param request ???????????????
	 * @param projectAndQuery ?????????????????????????????????
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

		// ?????? url ????????????????????????
		LinkedHashMap<String, Map> queryFormValues = buildFormValuesFromQueryParrameter(treeNode, request.getQuery());
		// ??????????????????url?????????????????????????????????
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
		// ????????????????????????
		return answer;
	}

	/**
	 * ?????????????????????????????? form values
	 * @param query
	 * @return
	 */
	private LinkedHashMap buildFormValuesFromQueryParrameter(SchemaHelper.TreeNode surveySchemaTreeNode,
			Map<String, String> query) {
		LinkedHashMap<String, Map> formValues = new LinkedHashMap<>();
		query.forEach((id, value) -> {
			// ???????????????
			SchemaHelper.TreeNode findNode = surveySchemaTreeNode.getTreeNodeMap().get(id);
			String questionId = findNode.getParent().getData().getId();
			Map questionValueMap = formValues.computeIfAbsent(questionId, k -> new HashMap<>());
			questionValueMap.put(id, value);
		});
		return formValues;
	}

	/**
	 * ??????????????????????????????like ??????
	 * @param qNode ??????????????? schema node ??????
	 * @param qValueObj ?????????????????????
	 * @return
	 */
	private String buildLikeQueryConditionOfQuestion(SchemaHelper.TreeNode qNode, Map qValueObj) {
		SurveySchema optionSchema = qNode.getData().getChildren().get(0);
		String optionId = optionSchema.getId();
		Object optionValue = qValueObj.get(optionId);
		String value = optionValue.toString();
		// ?????????????????????
		if (optionSchema.getAttribute() == null
				|| !SurveySchema.DataType.number.equals(optionSchema.getAttribute().getDataType())) {
			value = "\"" + value + "\"";
		}
		return String.format("{\"%s\":%s}", optionId, value);
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????
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
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param project ????????????
	 * @param answer ?????????????????????
	 * @return ????????????????????? schema???????????????????????????????????????????????????
	 */
	private SurveySchema convertAndValidateLoginFormIfNeeded(ProjectView project,
			LinkedHashMap<String, Object> answer) {
		boolean loginRequired = false;
		// ??????????????????????????????????????????
		boolean updatePartnerVisited = false;
		LambdaQueryWrapper<ProjectPartner> projectPartnerQuery = Wrappers.<ProjectPartner>lambdaQuery()
				.eq(ProjectPartner::getProjectId, project.getId());
		Authentication authentication = null;
		List<SurveySchema> queryConditions = new ArrayList<>();
		SurveySchema loginFormSchema = SurveySchema.builder().id(project.getId()).children(queryConditions)
				.title(project.getName()).build();

		if (project != null && project.getSetting() != null && project.getSetting().getAnswerSetting() != null) {
			ProjectSetting.AnswerSetting answerSetting = project.getSetting().getAnswerSetting();
			// ??????????????????
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
			// ??????????????????
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
			// ????????????????????????
			if (answerSetting.getWhitelistType() != null
					&& ProjectPartnerTypeEnum.RESPONDENT_SYS_USER.getType() == answerSetting.getWhitelistType()) {
				// ??????????????????????????????????????????????????????????????????
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
					// ????????????????????????
					SchemaHelper.appendChildIfNotExist(loginFormSchema,
							SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.username));
					SchemaHelper.appendChildIfNotExist(loginFormSchema,
							SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.password));
					loginRequired = true;

					if (answer != null) {
						if (authentication == null) {
							authentication = validateUsernameAndPassword(answer);
						}
						// ??????????????????
						UserInfo user = (UserInfo) authentication.getPrincipal();
						// ??????????????????????????????????????????
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
			// ????????????????????????
			if (answerSetting.getWhitelistType() != null
					&& ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == answerSetting.getWhitelistType()) {
				SchemaHelper.appendChildIfNotExist(loginFormSchema,
						SchemaHelper.buildFillBlankQuerySchema(SchemaHelper.LoginFormFieldEnum.whitelistName));
				loginRequired = true;

				if (answer != null) {
					// ?????????????????????????????????????????????????????????
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
			// ????????????????????????????????????????????????????????????????????????
			UserInfo user = (UserInfo) authentication.getPrincipal();
			HttpCookie cookie = ResponseCookie
					.from(AppConsts.COOKIE_TOKEN_NAME,
							jwtTokenUtil.generateAccessToken(new UserTokenView(user.getUserId())))
					.path("/").httpOnly(true).build();
			ContextHelper.getCurrentHttpResponse().setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		}

		// ??????????????????????????????
		if (updatePartnerVisited) {
			ProjectPartner projectPartner = projectPartnerMapper.selectOne(projectPartnerQuery);
			if (projectPartner != null) {
				projectPartner.setStatus(AppConsts.ProjectPartnerStatus.VISITED);
				projectPartnerMapper.updateById(projectPartner);
				// ????????????????????????????????? createBy ??? partner ??? id???????????????????????????????????? id
				if (projectPartner.getUserName() != null) {
					ContextHelper.getCurrentHttpRequest().setAttribute("createBy", projectPartner.getId());
				}
				else if (projectPartner.getUserId() != null) {
					ContextHelper.getCurrentHttpRequest().setAttribute("createBy", SecurityContextUtils.getUserId());
				}
			}
		}

		if (loginRequired) {
			return loginFormSchema;
		}

		return null;
	}

	/**
	 * ???????????????????????????????????????
	 * @param answer
	 * @return
	 */
	private Authentication validateUsernameAndPassword(LinkedHashMap<String, Object> answer) {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					SchemaHelper.getLoginFormAnswer(answer, SchemaHelper.LoginFormFieldEnum.username),
					SchemaHelper.getLoginFormAnswer(answer, SchemaHelper.LoginFormFieldEnum.password)));
		}
		catch (Exception e) {
			throw new ErrorCodeException(ErrorCode.ValidationError);
		}
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id?????????????????????????????????????????????????????????id???
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
			}
			else if (ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == whitelistType) {
				queryWrapper.eq(ProjectPartner::getUserName, request.getWhitelistName()).eq(ProjectPartner::getType,
						ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType());
			}

			ProjectPartner projectPartner = projectPartnerMapper.selectOne(queryWrapper);
			projectPartner.setStatus(AppConsts.ProjectPartnerStatus.ANSWERED);
			projectPartnerMapper.updateById(projectPartner);

			// ??????????????????????????????????????????????????? createBy ??? partner ??? id
			if (ProjectPartnerTypeEnum.RESPONDENT_IMP_USER.getType() == whitelistType) {
				AnswerRequest answerUpdateRequest = new AnswerRequest();
				answerUpdateRequest.setId(request.getId());
				answerUpdateRequest.setCreateBy(projectPartner.getId());
				answerService.updateAnswer(answerUpdateRequest);
			}

		}
	}

}
