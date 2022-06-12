package cn.surveyking.server.flow.service.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.core.uitls.SchemaHelper;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.flow.constant.*;
import cn.surveyking.server.flow.domain.dto.*;
import cn.surveyking.server.flow.domain.mapper.FlowEntryElementModelMapper;
import cn.surveyking.server.flow.domain.mapper.FlowEntryModelMapper;
import cn.surveyking.server.flow.domain.mapper.FlowOperationModelMapper;
import cn.surveyking.server.flow.domain.model.*;
import cn.surveyking.server.flow.exception.FlowableRuntimeException;
import cn.surveyking.server.flow.service.*;
import cn.surveyking.server.flow.service.impl.taskHandler.RevertTaskHandler;
import cn.surveyking.server.flow.service.impl.taskHandler.TaskHandler;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.ProjectService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowServiceImpl implements FlowService {

	private final FlowEntryService entryService;

	private final FlowEntryNodeService entryNodeService;

	private final FlowEntryPublishService entryPublishService;

	private final UserService userService;

	private final AnswerService answerService;

	private final ProjectService projectService;

	private final RepositoryService repositoryService;

	private final FlowEntryModelMapper entryModelMapper;

	private final FlowEntryElementModelMapper entryNodeModelMapper;

	private final FlowOperationModelMapper flowOperationModelMapper;

	private final FlowInstanceService flowInstanceService;

	private final FlowOperationService flowOperationService;

	private final TaskService taskService;

	private final RuntimeService runtimeService;

	private final RevertTaskHandler revertTaskHandler;

	/**
	 * 问卷开始之前，根据权限过滤字段。
	 * @param projectView
	 * @return
	 */
	@Override
	public void beforeLaunchProcess(PublicProjectView projectView) {
		SurveySchema schema = projectView.getSurvey();
		if (schema == null) {
			return;
		}
		FlowEntryNode node = entryNodeService.getById(schema.getId());
		// 未设置流程
		if (node == null) {
			return;
		}
		// 流程申请人为空
		if (node.getIdentity() == null) {
			SchemaHelper.updateSchemaByPermission(node.getFieldPermission(), schema);
			return;
		}
		// 需要登录，如开启了工作流或者设置了成员/部门题都需要登录才能答卷
		if (!SecurityContextUtils.isAuthenticated()) {
			projectView.setLoginRequired(true);
			projectView.setSurvey(null);
			return;
		}
		String userId = SecurityContextUtils.getUserId();
		Set<String> userGroups = userService.getUserGroups(userId);
		for (String identity : node.getIdentity()) {
			if (userGroups.contains(identity)) {
				SchemaHelper.updateSchemaByPermission(node.getFieldPermission(), schema);
				return;
			}
		}
		// 用户没有发起流程权限
		projectView.setSurvey(null);
	}

	@Override
	public void saveFlow(FlowEntryRequest request) {
		FlowEntry flow = entryService
				.getOne(Wrappers.<FlowEntry>lambdaQuery().eq(FlowEntry::getProjectId, request.getProjectId()));
		if (flow == null) {
			flow = new FlowEntry();
			flow.setProjectId(request.getProjectId());
			flow.setBpmnXml(request.getBpmnXml());
			flow.setNodes(entryNodeModelMapper.fromRequest(request.getNodes()));
			entryService.save(flow);
		}
		else {
			flow.setBpmnXml(request.getBpmnXml());
			flow.setNodes(entryNodeModelMapper.fromRequest(request.getNodes()));
			entryService.updateById(flow);
		}
	}

	@Override
	public void deploy(String projectId) {
		FlowEntry flowEntry = entryService
				.getOne(Wrappers.<FlowEntry>lambdaQuery().eq(FlowEntry::getProjectId, projectId));
		if (flowEntry == null) {
			throw new FlowableRuntimeException("该问卷未设置流程");
		}
		try {
			InputStream xmlStream = new ByteArrayInputStream(flowEntry.getBpmnXml().getBytes(StandardCharsets.UTF_8));
			@Cleanup
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(xmlStream);
			BpmnXMLConverter converter = new BpmnXMLConverter();
			BpmnModel bpmnModel = converter.convertToBpmnModel(reader);

			Deployment deployment = repositoryService.createDeployment()
					.addBpmnModel(flowEntry.getId() + ".bpmn20.xml", bpmnModel).deploy();
			ProcessDefinition pd = getProcessDefinitionByDeployId(deployment.getId());

			flowEntry.setDeployId(deployment.getId());
			flowEntry.setProjectId(projectId);
			entryService.updateById(flowEntry);

			// 更新之前版本为历史版本
			FlowEntryPublish entryPublished = new FlowEntryPublish();
			entryPublished.setMainVersion(false);
			entryPublished.setActiveStatus(false);
			entryPublishService.update(entryPublished,
					Wrappers.<FlowEntryPublish>lambdaUpdate().eq(FlowEntryPublish::getEntryId, flowEntry.getId()));

			// 发布新版本
			FlowEntryPublish entryPublish = new FlowEntryPublish();
			entryPublish.setId(deployment.getId());
			entryPublish.setEntryId(flowEntry.getId());
			entryPublish.setPublishTime(new Date());
			entryPublish.setActiveStatus(true);
			entryPublish.setPublishVersion(pd.getVersion());
			entryPublish.setProcessDefinitionId(pd.getId());
			entryPublish.setMainVersion(true);
			entryPublish.setActiveStatus(true);
			entryPublishService.save(entryPublish);

			// 更新流程节点
			saveFlowElement(flowEntry);

			// 更新流程信息
			flowEntry.setProcessDefinitionId(pd.getId());
			flowEntry.setStatus(FlowEntryStatus.PUBLISHED);
			entryService.updateById(flowEntry);
		}
		catch (Exception e) {
			throw new FlowableRuntimeException("流程" + flowEntry.getId() + "部署失败", e);
		}
	}

	@Override
	public FlowEntryView getFlowEntry(String projectId) {
		FlowEntry flow = entryService.getOne(Wrappers.<FlowEntry>lambdaQuery().eq(FlowEntry::getProjectId, projectId));
		if (flow == null) {
			flow = new FlowEntry();
			flow.setProjectId(projectId);
		}
		FlowEntryView result = entryModelMapper.toView(flow);
		result.setNodes(entryNodeModelMapper.toView(flow.getNodes()));
		return result;
	}

	@Override
	public void approvalTask(ApprovalTaskRequest request) {
		TaskHandler taskHandler = (TaskHandler) ContextHelper.getBean(request.getType() + "TaskHandler");
		taskHandler.process(request);
	}

	@Override
	public PaginationResponse<FlowTaskView> getFlowTasks(FlowTaskQuery query) {
		PaginationResponse<FlowTaskView> result = null;
		if (query.getType() == FlowTaskQueryType.todo) {
			result = getTodo(query);
		}
		if (query.getType() == FlowTaskQueryType.finished) {
			result = getFinished(query);
		}
		if (query.getType() == FlowTaskQueryType.selfCreated) {
			result = getSelfCreated(query);
		}
		if (result != null) {
			setFlowTaskAnswer(result.getList());
			setTaskStatus(result.getList());
		}
		return result;
	}

	private PaginationResponse<FlowTaskView> getTodo(FlowTaskQuery query) {
		TaskQuery taskQuery = taskService.createTaskQuery().active();
		String userId = SecurityContextUtils.getUserId();
		// https://forum.flowable.org/t/sql-exception-with-task-query-after-upgrade-to-6-7-0/8334
		taskQuery.processDefinitionKey(query.getProjectId()).or().taskCandidateUser(userId).taskAssignee(userId).endOr()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		int firstResult = (query.getCurrent() - 1) * query.getPageSize();
		List<Task> taskList = taskQuery.listPage(firstResult, query.getPageSize());
		long totalCount = taskQuery.count();

		List<FlowTaskView> viewList = taskList.stream().map(task -> {
			FlowTaskView taskView = new FlowTaskView();
			taskView.setId(task.getId());
			taskView.setCreateAt(task.getCreateTime());
			taskView.setProjectId(query.getProjectId());
			String answerId = (String) task.getProcessVariables().get(FlowConstant.VARIABLE_ANSWER_KEY);
			taskView.setAnswerId(answerId);
			taskView.setActivityId(task.getTaskDefinitionKey());
			taskView.setProcessInstanceId(task.getProcessInstanceId());
			return taskView;
		}).collect(Collectors.toList());

		return new PaginationResponse<>(totalCount, viewList);
	}

	private PaginationResponse<FlowTaskView> getFinished(FlowTaskQuery query) {
		Page<FlowOperation> page = new Page<>(query.getCurrent(), query.getPageSize());

		flowOperationService.page(page, Wrappers.<FlowOperation>lambdaQuery()
				.eq(FlowOperation::getProjectId, query.getProjectId())
				.eq(FlowOperation::getTaskType, FlowTaskType.userTask)
				.ne(FlowOperation::getApprovalType, FlowApprovalType.SAVE)
				.exists(String.format(
						"select 1 from t_flow_operation_user u where u.latest = 1 and u.operation_id = t_flow_operation.id and u.user_id = '%s'",
						SecurityContextUtils.getUserId()))
				.orderByDesc(FlowOperation::getCreateAt));
		List<FlowTaskView> viewList = page.getRecords().stream().map(opt -> {
			FlowTaskView flowTaskView = new FlowTaskView();
			flowTaskView.setId(opt.getId());
			flowTaskView.setActivityId(opt.getActivityId());
			flowTaskView.setProcessInstanceId(opt.getInstanceId());
			flowTaskView.setAnswerId(opt.getAnswerId());
			flowTaskView.setProjectId(opt.getProjectId());
			flowTaskView.setApprovalType(opt.getApprovalType());
			flowTaskView.setLatest(opt.getLatest());
			return flowTaskView;
		}).collect(Collectors.toList());
		return new PaginationResponse<>(page.getTotal(), viewList);
	}

	private PaginationResponse<FlowTaskView> getSelfCreated(FlowTaskQuery query) {
		Page<FlowInstance> page = new Page<>(query.getCurrent(), query.getPageSize());
		flowInstanceService.page(page, Wrappers.<FlowInstance>lambdaQuery()
				.eq(FlowInstance::getProjectId, query.getProjectId()).orderByDesc(FlowInstance::getCreateAt));
		List<FlowTaskView> viewList = page.getRecords().stream().map(instance -> {
			FlowTaskView flowTaskView = new FlowTaskView();
			flowTaskView.setCreateAt(instance.getCreateAt());
			flowTaskView.setApprovalStage(instance.getApprovalStage());
			flowTaskView.setStatus(instance.getStatus());
			flowTaskView.setAnswerId(instance.getAnswerId());
			flowTaskView.setProcessInstanceId(instance.getId());
			flowTaskView.setProjectId(instance.getProjectId());
			flowTaskView.setActivityId(instance.getProjectId());
			flowTaskView.setCreateAt(instance.getCreateAt());
			flowTaskView.setUpdateAt(instance.getUpdateAt());
			return flowTaskView;
		}).collect(Collectors.toList());
		return new PaginationResponse<>(page.getTotal(), viewList);
	}

	@Override
	public SurveySchema loadSchemaByPermission(SchemaQuery query) {
		ProjectView projectView = projectService.getProject(query.getProjectId());
		SurveySchema schema = projectView.getSurvey();
		FlowEntryNode element = entryNodeService.getById(query.getTaskDefKey());
		if (element == null) {
			return schema;
		}
		SchemaHelper.updateSchemaByPermission(element.getFieldPermission(), schema);
		return schema;
	}

	@Override
	public List<FlowOperationView> getAuditRecord(String processInstanceId) {
		List<FlowOperation> operations = flowOperationService
				.list(Wrappers.<FlowOperation>lambdaQuery().eq(FlowOperation::getInstanceId, processInstanceId)
						.eq(FlowOperation::getTaskType, FlowTaskType.userTask).orderByAsc(FlowOperation::getCreateAt));
		List<FlowOperationView> operationViews = flowOperationModelMapper.toView(operations);
		FlowInstance flowInstance = flowInstanceService.getById(processInstanceId);
		if (FlowInstanceStatus.APPROVING == flowInstance.getStatus()) {
			// 当前任务正在审批，设置审批人
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
					.onlyChildExecutions().singleResult();
			if (execution != null) {
				FlowOperationView runningOperationView = new FlowOperationView();
				runningOperationView.setActivityId(execution.getActivityId());
				runningOperationView.setApprovalType(FlowApprovalType.TODO);
				operationViews.add(runningOperationView);
				List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId)
						.includeIdentityLinks().list();
				Set<String> waitAuditUserIds = new LinkedHashSet<>();
				tasks.forEach(t -> {
					if (t.getAssignee() != null) {
						waitAuditUserIds.add(t.getAssignee());
					}
					t.getIdentityLinks().forEach(link -> {
						waitAuditUserIds.add(link.getUserId());
					});
				});
				// 设置等待审核用户列表
				runningOperationView.setWaitAuditUserList(waitAuditUserIds.stream()
						.map(uid -> userService.loadUserById(uid)).collect(Collectors.toList()));
			}
		}
		else if (FlowInstanceStatus.SUSPENDED == flowInstance.getStatus()) {
			// 需要申请人完善
			FlowOperationView runningOperationView = new FlowOperationView();
			runningOperationView.setActivityId(FlowConstant.STARTER_ACTIVITY_ID);
			runningOperationView.setActivityName(FlowConstant.STARTER_ACTIVITY_NAME);
			runningOperationView.setApprovalType(FlowApprovalType.TODO);
			runningOperationView.setWaitAuditUserList(
					Collections.singletonList(userService.loadUserById(flowInstance.getCreateBy())));
			operationViews.add(runningOperationView);
		}

		// 添加历史节点
		operationViews.forEach(view -> {
			if (view.getCreateBy() != null) {
				view.setAuditUser(userService.loadUserById(view.getCreateBy()));
			}
			if (view.getActivityId() != null) {
				FlowEntryNode node = entryNodeService.getById(view.getActivityId());
				if (node != null) {
					view.setActivityName(node.getName());
				}
			}
			if (view.getNewActivityId() != null) {
				if (FlowConstant.STARTER_ACTIVITY_ID.equals(view.getNewActivityId())) {
					view.setNewActivityName(FlowConstant.STARTER_ACTIVITY_NAME);
				}
				else {
					FlowEntryNode node = entryNodeService.getById(view.getNewActivityId());
					if (node != null) {
						view.setNewActivityName(node.getName());
					}
				}
			}
			if (view.getApprovalType() != null) {
				view.setApprovalTypeName(FlowApprovalType.DICT_MAP.get(view.getApprovalType()));
			}
			if (!StringUtils.hasText(view.getActivityName()) && FlowApprovalType.SAVE.equals(view.getApprovalType())) {
				view.setActivityName("申请人");
			}
		});

		return operationViews;
	}

	@Override
	public List<RevokeView> getRevertNodes(String processInstanceId) {
		return revertTaskHandler.getRevertNodes(processInstanceId).stream().map(node -> {
			RevokeView view = new RevokeView();
			view.setActivityId(node.getActivityId());
			view.setActivityName(entryNodeService.getById(node.getActivityId()).getName());
			return view;
		}).collect(Collectors.toList());
	}

	@Override
	public FlowStaticsView statics() {
		FlowStaticsView statics = new FlowStaticsView();
		// 获取我的待办
		TaskQuery taskQuery = taskService.createTaskQuery().active();
		String userId = SecurityContextUtils.getUserId();
		taskQuery.or().taskCandidateUser(userId).taskAssignee(userId).endOr().orderByTaskCreateTime().desc();
		statics.setTodo(taskQuery.count());
		// 获取我的已办
		statics.setFinished(flowOperationService
				.count(Wrappers.<FlowOperation>lambdaQuery().eq(FlowOperation::getTaskType, FlowTaskType.userTask)
						.ne(FlowOperation::getApprovalType, FlowApprovalType.SAVE)));
		// 获取我能发起的，任务在审批中的
		statics.setSelfCreated(flowInstanceService.count(
				Wrappers.<FlowInstance>lambdaQuery().eq(FlowInstance::getCreateBy, SecurityContextUtils.getUserId())
						.eq(FlowInstance::getStatus, FlowInstanceStatus.APPROVING)));
		// 获取抄送给我的
		return statics;
	}

	private ProcessDefinition getProcessDefinitionByDeployId(String deployId) {
		return repositoryService.createProcessDefinitionQuery().deploymentId(deployId).latestVersion().singleResult();
	}

	private void setFlowTaskAnswer(List<FlowTaskView> views) {
		for (FlowTaskView view : views) {
			String answerId = view.getAnswerId();
			AnswerQuery answerQuery = new AnswerQuery();
			answerQuery.setId(answerId);
			AnswerView answerView = answerService.getAnswer(answerQuery);
			UserInfo createUser = userService.loadUserById(answerView.getCreateBy());
			if (createUser != null) {
				view.setCreateUser(createUser.simpleMode());
			}
			FlowEntryNode node = entryNodeService.getById(view.getActivityId());
			view.setFieldPermission(node.getFieldPermission());
			filterAnswerByPermission(answerView.getAnswer(), node.getFieldPermission());
			view.setAnswer(answerView.getAnswer());
			view.setAttachment(answerView.getAttachment());
			view.setUsers(answerView.getUsers());
			view.setDepts(answerView.getDepts());
		}
	}

	/**
	 * 设置当前任务的任务状态
	 * @param views
	 */
	private void setTaskStatus(List<FlowTaskView> views) {
		views.forEach(view -> {
			if (view.getStatus() != null) {
				return;
			}
			FlowInstance instance = flowInstanceService.getById(view.getProcessInstanceId());
			view.setStatus(instance.getStatus());
			view.setApprovalStage(instance.getApprovalStage());
			view.setCreateAt(instance.getCreateAt());
			view.setUpdateAt(instance.getUpdateAt());
		});
	}

	/**
	 * 保存流程节点，将 flowEntry 里面的临时节点保存到数据库
	 * @param flowEntry
	 */
	private void saveFlowElement(FlowEntry flowEntry) {
		// 不能直接删除当前项目下面的所有流程节点，因为多次部署，旧的部署版本可能需要之前的流程节点
		entryNodeService
				.removeBatchByIds(flowEntry.getNodes().stream().map(x -> x.getId()).collect(Collectors.toList()));
		if (flowEntry.getNodes() != null) {
			flowEntry.getNodes().forEach(node -> {
				node.setProjectId(flowEntry.getProjectId());
				entryNodeService.save(node);
			});
		}
	}

	private void filterAnswerByPermission(LinkedHashMap<String, Object> answer,
			LinkedHashMap<String, Integer> fieldPermission) {
		answer.entrySet().removeIf(entry -> {
			Integer valuePermission = fieldPermission.get(entry.getKey());
			if (valuePermission == null) {
				return false;
			}
			if (valuePermission == 0) {
				return true;
			}
			return false;
		});
	}

}
