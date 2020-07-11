package com.miaojiaosan.activiti.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaojiaosan.activiti.Application;
import com.miaojiaosan.activiti.param.Definition;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.HistoricProcessInstanceQueryImpl;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.history.DefaultHistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-10 9:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ActivitiTest {

	String json = "{\n" +
		"  \"process\": {\n" +
		"    \"processKey\": \"holiday\",\n" +
		"    \"name\": \"请假流程\"\n" +
		"  },\n" +
		"\n" +
		"  \"processNodes\": [\n" +
		"    {\n" +
		"      \"nodeKey\": \"start\",\n" +
		"      \"name\": \"开始\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"startEvent\",\n" +
		"        \"initiator\": 10086\n" +
		"      }\n" +
		"    },{\n" +
		"      \"nodeKey\": \"_11111\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"sequenceFlow\",\n" +
		"        \"sourceRef\": \"start\",\n" +
		"        \"targetRef\": \"audit\"\n" +
		"      }\n" +
		"    },{\n" +
		"      \"nodeKey\": \"audit\",\n" +
		"      \"name\": \"班主任审批\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"userTask\",\n" +
		"        \"approval\": {\n" +
		"          \"auditType\": 1,\n" +
		"          \"countersign\": 1,\n" +
		"          \"users\":[\n" +
		"            10086\n"+
		"          ]"+
		"        }\n" +
		"      }\n" +
		"    },{\n" +
		"\n" +
		"      \"nodeKey\": \"_22222\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"sequenceFlow\",\n" +
		"        \"sourceRef\": \"audit\",\n" +
		"        \"targetRef\": \"audit2\"\n" +
		"      }\n" +
		"    },{\n" +
		"      \"nodeKey\": \"audit2\",\n" +
		"      \"name\": \"教务处主任审批\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"userTask\",\n" +
		"        \"approval\": {\n" +
		"          \"auditType\": 2,\n" +
		"          \"level\": 1,\n" +
		"          \"whenOrgNotExists\": 10086\n" +
		"        }\n" +
		"      }\n" +
		"    },{\n" +
		"\n" +
		"      \"nodeKey\": \"_3333\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"sequenceFlow\",\n" +
		"        \"sourceRef\": \"audit2\",\n" +
		"        \"targetRef\": \"audit3\"\n" +
		"      }\n" +
		"    },{\n" +
		"\n" +
		"      \"nodeKey\": \"audit3\",\n" +
		"      \"name\": \"校长审批\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"userTask\",\n" +
		"        \"approval\": {\n" +
		"          \"auditType\": 3,\n" +
		"          \"approverIdKey\": 100010\n" +
		"        }\n" +
		"      }\n" +
		"    },{\n" +
		"      \"nodeKey\": \"_44444\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"sequenceFlow\",\n" +
		"        \"sourceRef\": \"audit3\",\n" +
		"        \"targetRef\": \"end\"\n" +
		"      }\n" +
		"    },{\n" +
		"      \"nodeKey\": \"end\",\n" +
		"      \"name\": \"结束\",\n" +
		"      \"independence\": {\n" +
		"        \"nodeType\": \"endEvent\"\n" +
		"      }\n" +
		"    }\n" +
		"  ]\n" +
		"}";

	@Autowired
	private RepositoryService repository;


	public BpmnModel validate() throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		Definition definition = objectMapper.readValue(json, Definition.class);

		Process process = definition.getProcess().create();
		definition.getProcessNodes().forEach(processNode -> {
			process.addFlowElement(processNode.create());
		});

		BpmnModel bpmnModel = new BpmnModel();
		bpmnModel.setTargetNamespace("http://www.activiti.org/testm1574124674914");
		bpmnModel.addProcess(process);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
		assert validate.size() == 0;

		return bpmnModel;
	}


	public Deployment  deploy() throws JsonProcessingException {
		BpmnModel bpmnModel = validate();

		DeploymentBuilder deployment = repository.createDeployment();
		Deployment deploy = deployment.addBpmnModel("bpmnModel测试部署.bpmn", bpmnModel)
			.name("bpmnModel测试部署")
			.key("holiday222222")
			.category(bpmnModel.getTargetNamespace())
			.deploy();
		assert Objects.nonNull(deploy.getId());
		return deploy;
	}

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private IdentityService identityService;

	public ProcessInstance run () throws JsonProcessingException {
		Deployment deploy = deploy();
		identityService.setAuthenticatedUserId("10000");
		ProcessInstance processInstance =
			runtimeService.startProcessInstanceByKey("holiday", new HashMap<String, Object>(1){{
				put("startUser","10000");
			}});
		assert Objects.nonNull(processInstance.getId());
		return processInstance;
	}

	@Autowired
	private TaskService taskService;

	@Autowired
	private ManagementService managementService;

	@Test
	public void complete() throws JsonProcessingException {
		ProcessInstance instance = run();
		Task task
			= taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateOrAssigned("10086").singleResult();
		taskService.setVariable(task.getId(),"result","refuse");
//		taskService.complete(task.getId());
		taskService.claim(task.getId(),"10010");
		Process process = repository.getBpmnModel(task.getProcessDefinitionId()).getMainProcess();

		FlowNode end = (FlowNode)process.getFlowElement("end");

		String executeId = managementService.executeCommand(new DeleteTaskCmd(task.getId()));

		managementService.executeCommand(new SetFlowNodeAndGoCmd(end,task, task.getExecutionId()));


	}

	public static class DeleteTaskCmd extends NeedsActiveTaskCmd<String> {


		public DeleteTaskCmd(String taskId) {
			super(taskId);
		}

		@Override
		protected String execute(CommandContext commandContext, TaskEntity task) {

			TaskEntityManager taskEntityManager = Context.getCommandContext().getTaskEntityManager();

			ExecutionEntity execution = task.getExecution();

			taskEntityManager.deleteTask(task, "审批不通过", false, false);

			return execution.getId();
		}
	}

	public static class SetFlowNodeAndGoCmd implements Command<Void> {

		FlowNode node;
		String executionId;
		Task task;

		public SetFlowNodeAndGoCmd(FlowNode node, Task task, String executionId){
			this.node = node;
			this.executionId = executionId;
			this.task = task;
		}


		@Override
		public Void execute(CommandContext commandContext) {

			List<SequenceFlow> incomingFlows = this.node.getIncomingFlows();

			ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findById(executionId);
			executionEntity.setCurrentFlowElement(incomingFlows.get(0));
			HistoricActivityInstanceEntityManager activityInstanceEntityManager = commandContext.getHistoricActivityInstanceEntityManager();

			List<HistoricActivityInstanceEntity> unfinished
				= activityInstanceEntityManager.findUnfinishedHistoricActivityInstancesByExecutionAndActivityId(executionEntity.getId(), task.getTaskDefinitionKey());
			for (HistoricActivityInstanceEntity entity : unfinished) {
				if(Objects.equals(entity.getTaskId(), task.getId())){
					long timeMillis = System.currentTimeMillis();
					entity.setEndTime(new Date(timeMillis));
					entity.setDurationInMillis(timeMillis - entity.getStartTime().getTime());
					activityInstanceEntityManager.update(entity);
				}
			}

			commandContext.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);
			return null;
		}
	}
}
