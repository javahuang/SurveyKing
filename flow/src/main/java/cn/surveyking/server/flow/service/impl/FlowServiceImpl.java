package cn.surveyking.server.flow.service.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.uitls.ContextHelper;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.flow.constant.*;
import cn.surveyking.server.flow.domain.dto.*;
import cn.surveyking.server.flow.domain.mapper.FlowEntryModelMapper;
import cn.surveyking.server.flow.domain.mapper.FlowEntryElementModelMapper;
import cn.surveyking.server.flow.domain.model.FlowEntry;
import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import cn.surveyking.server.flow.domain.model.FlowEntryPublish;
import cn.surveyking.server.flow.domain.model.FlowInstance;
import cn.surveyking.server.flow.exception.FlowableRuntimeException;
import cn.surveyking.server.flow.service.*;
import cn.surveyking.server.flow.service.impl.taskHandler.TaskHandler;
import cn.surveyking.server.service.AnswerService;
import cn.surveyking.server.service.ProjectService;
import cn.surveyking.server.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
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

	private final FlowInstanceService flowInstanceService;

	private final TaskService taskService;

	/**
	 * 问卷开始之前，根据权限过滤字段。
	 * @param userId 当前
	 * @param schema
	 * @return
	 */
	@Override
	public SurveySchema beforeLaunchProcess(String userId, SurveySchema schema) {
		if (schema == null) {
			return null;
		}
		FlowEntryNode node = entryNodeService.getOne(Wrappers.<FlowEntryNode>lambdaQuery()
				.eq(FlowEntryNode::getTaskType, FlowTaskType.starter).eq(FlowEntryNode::getActivityId, schema.getId()));
		// 未设置流程
		if (node == null) {
			return schema;
		}
		// 流程申请人为空
		if (node.getIdentity() == null) {
			updateSchemaByPermission(node.getFieldPermission(), schema);
			return schema;
		}
		Set<String> userGroups = userService.getUserGroups(userId);
		for (String identity : node.getIdentity()) {
			if (userGroups.contains(identity)) {
				updateSchemaByPermission(node.getFieldPermission(), schema);
				return schema;
			}
		}
		// 用户没有发起流程权限
		return null;
	}

	@Override
	public void saveFlow(FlowEntryRequest request) {
		FlowEntry flow = entryService
				.getOne(Wrappers.<FlowEntry>lambdaQuery().eq(FlowEntry::getProjectId, request.getProjectId()));
		if (flow == null) {
			flow = new FlowEntry();
			flow.setProjectId(request.getProjectId());
			flow.setBpmnXml(request.getBpmnXml());
			entryService.save(flow);
		}
		else {
			flow.setBpmnXml(request.getBpmnXml());
			entryService.updateById(flow);
		}
		saveFlowElement(request, flow.getId());
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
			flowEntry.setProjectId(deployment.getKey());
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

			// 更新流程信息
			flowEntry.setMainEntryPublishId(entryPublish.getId());
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
		result.setNodes(entryNodeModelMapper.toView(entryNodeService
				.list(Wrappers.<FlowEntryNode>lambdaQuery().eq(FlowEntryNode::getProjectId, flow.getProjectId()))));
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
		if (result != null) {
			setFlowTaskAnswer(result.getList());
			setTaskStatus(result.getList());
		}
		return result;
	}

	private PaginationResponse<FlowTaskView> getTodo(FlowTaskQuery query) {
		TaskQuery taskQuery = taskService.createTaskQuery().active();
		taskQuery.processDefinitionKey(query.getProjectId()).includeProcessVariables();
		String userId = SecurityContextUtils.getUserId();
		Set<String> userGroups = userService.getUserGroups(userId);
		taskQuery.or().taskCandidateOrAssigned("U:" + userId).taskCandidateGroupIn(userGroups).endOr();
		taskQuery.orderByTaskCreateTime().desc();
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

	@Override
	public SurveySchema loadSchemaByPermission(SchemaQuery query) {
		ProjectQuery projectQuery = new ProjectQuery();
		projectQuery.setId(query.getProjectId());
		ProjectView projectView = projectService.getProject(projectQuery);
		SurveySchema schema = projectView.getSurvey();
		FlowEntryNode element = entryNodeService
				.getOne(Wrappers.<FlowEntryNode>lambdaQuery().eq(FlowEntryNode::getProjectId, query.getProjectId())
						.eq(FlowEntryNode::getActivityId, query.getTaskDefKey()));
		if (element == null) {
			return schema;
		}
		updateSchemaByPermission(element.getFieldPermission(), schema);
		return schema;
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
			view.setAttachment(answerView.getAttachment());
			UserInfo createUser = userService.loadUserById(answerView.getCreateBy());
			if (createUser != null) {
				view.setCreateUser(createUser.simpleMode());
			}
			FlowEntryNode node = entryNodeService
					.getOne(Wrappers.<FlowEntryNode>lambdaQuery().eq(FlowEntryNode::getProjectId, view.getProjectId())
							.eq(FlowEntryNode::getActivityId, view.getActivityId()));
			filterAnswerByPermission(answerView.getAnswer(), node.getFieldPermission());
			view.setAnswer(answerView.getAnswer());
		}
	}

	/**
	 * 设置当前任务的任务状态
	 * @param views
	 */
	private void setTaskStatus(List<FlowTaskView> views) {
		views.forEach(view -> {
			FlowInstance instance = flowInstanceService.getById(view.getProcessInstanceId());
			FlowTaskStatus.getDictStatus(instance.getStatus());
			int taskStatus = instance.getStatus();
			if (taskStatus == FlowTaskStatus.APPROVING) {
				// 正在审批时，获取当前审批节点名称
				view.setTaskStatus(instance.getApprovalStage());
			}
			else {
				view.setTaskStatus(FlowTaskStatus.getDictStatus(taskStatus));
			}
		});
	}

	private void saveFlowElement(FlowEntryRequest request, String flowId) {
		entryNodeService
				.remove(Wrappers.<FlowEntryNode>lambdaQuery().eq(FlowEntryNode::getProjectId, request.getProjectId()));
		if (request.getNodes() != null) {
			request.getNodes().forEach(node -> {
				node.setProjectId(request.getProjectId());
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

	private void updateSchemaByPermission(LinkedHashMap<String, Integer> fieldPermission, SurveySchema schema) {
		if (schema.getChildren() == null || fieldPermission == null) {
			return;
		}
		schema.getChildren().removeIf(child -> {
			Integer permValue = fieldPermission.get(child.getId());
			if (permValue == null) {
				return false;
			}
			// 隐藏题目
			if (permValue == FieldPermissionType.hidden) {
				return true;
			}
			// 只读
			if (permValue == FieldPermissionType.visible) {
				if (child.getAttribute() == null) {
					child.setAttribute(new SurveySchema.Attribute());
				}
				child.getAttribute().setReadOnly(true);
			}
			return false;
		});
		schema.getChildren().forEach(child -> updateSchemaByPermission(fieldPermission, child));
	}

}
