package com.miaojiaosan.activiti.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaojiaosan.activiti.Application;
import de.odysseus.el.TreeValueExpression;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.delegate.invocation.ExpressionGetInvocation;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ApplicationContextElResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.el.CompositeELResolver;
import javax.el.ValueExpression;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Objects;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-07 14:01
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Repository {

	@Autowired
	private RepositoryService repository;


	@Test
	public void deploy() throws JsonProcessingException {

		BpmnModel bpmnModel = Parse.parse2();

		repository.newModel();
		DeploymentBuilder deployment = repository.createDeployment();
		Deployment deploy = deployment.addBpmnModel("bpmnModel测试部署.bpmn", bpmnModel)
			.name("bpmnModel测试部署")
			.key("holiday222222")
			.category(bpmnModel.getTargetNamespace())
			.deploy();
		run();
	}

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;


	@Test
	public void run(){
		String zhangsan = "zhangsan";
		identityService.setAuthenticatedUserId(zhangsan);
		HashMap<String, Object> variable = new HashMap<>();
		//设置发起人
		variable.put("startUser", zhangsan);
		//发起流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday",variable);
		assert Objects.nonNull(processInstance.getId());
	}

	@Test

	public void complete() {
		//查询任务
		Task task = taskService.createTaskQuery()
			.taskAssignee("10086")
//			.taskCandidateUser("10086")
			.singleResult();
		assert Objects.nonNull(task);
		//审批意见
//		taskService.addComment(task.getId(),processInstance.getId(),"填写请假单");
		//完成任务
//		taskService.claim(task.getId(),"10086");
		runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "驳回");
//		taskService.complete(task.getId());
	}


	@Test
	public void complete2() {

		//查询任务
		Task task = taskService.createTaskQuery().taskCandidateGroup("group1").or()
			.taskAssignee("lisi").or().taskCandidateUser("lisi").singleResult();
		assert Objects.nonNull(task);

	}

	@Test
	public void test() throws XMLStreamException, JsonProcessingException {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/testm1574124674914\">\n" +
			"  <process id=\"holiday\" name=\"请假流程\" isExecutable=\"true\">\n" +
			"    <startEvent id=\"start\" name=\"开始\" activiti:initiator=\"10086\"></startEvent>\n" +
			"    <sequenceFlow id=\"_11111\" sourceRef=\"start\" targetRef=\"audit\"></sequenceFlow>\n" +
			"    <userTask id=\"audit\" name=\"班主任审批\" activiti:candidateUsers=\"10086,10010\">\n" +
			"      <multiInstanceLoopCharacteristics activiti:collection=\"assigneeList\" activiti:elementVariable=\"assignee\">\n" +
			"        <loopCardinality xsi:type=\"tFormalExpression\">3</loopCardinality>\n" +
			"      </multiInstanceLoopCharacteristics>\n" +
			"    </userTask>\n" +
			"    <sequenceFlow id=\"_22222\" sourceRef=\"audit\" targetRef=\"audit2\"></sequenceFlow>\n" +
			"    <userTask id=\"audit2\" name=\"教务处主任审批\" activiti:assignee=\"${assignee}\" activiti:level=\"1\" activiti:whenOrgNotExists=\"10086\">\n" +
			"      <extensionElements>\n" +
			"        <activiti:taskListener event=\"start\" delegateExpression=\"${RefuseListener}\"></activiti:taskListener>\n" +
			"      </extensionElements>\n" +
			"    </userTask>\n" +
			"    <sequenceFlow id=\"_3333\" sourceRef=\"audit2\" targetRef=\"audit3\"></sequenceFlow>\n" +
			"    <userTask id=\"audit3\" name=\"校长审批\" activiti:assignee=\"${assignee}\" activiti:approverIdKey=\"100010\"></userTask>\n" +
			"    <sequenceFlow id=\"_44444\" sourceRef=\"audit3\" targetRef=\"end\"></sequenceFlow>\n" +
			"    <endEvent id=\"end\" name=\"结束\"></endEvent>\n" +
			"  </process>\n" +
			"  <bpmndi:BPMNDiagram id=\"BPMNDiagram_holiday\">\n" +
			"    <bpmndi:BPMNPlane bpmnElement=\"holiday\" id=\"BPMNPlane_holiday\"></bpmndi:BPMNPlane>\n" +
			"  </bpmndi:BPMNDiagram>\n" +
			"</definitions>";

		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(xml.getBytes());
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(tInputStringStream);
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(reader);
		ObjectMapper objectMapper = new ObjectMapper();
//		byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);
//		String str = new String(bytes, Charset.defaultCharset());
//		System.out.println(str);
		String s = objectMapper.writeValueAsString(bpmnModel);
		System.out.println(s);
	}

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void test2(){
		ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();


	}
}
