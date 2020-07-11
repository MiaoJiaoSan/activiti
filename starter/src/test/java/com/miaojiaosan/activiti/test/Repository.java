package com.miaojiaosan.activiti.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaojiaosan.activiti.Application;
import com.miaojiaosan.activiti.param.Definition;
import com.miaojiaosan.activiti.param.ProcessNode;
import com.miaojiaosan.activiti.param.UserTask;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
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
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
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

	@Test
	public void simpleFlow() throws JsonProcessingException {
		String json = "{\n" +
				"  \"process\": {\n" +
				"    \"processKey\": \"simpleFLow\",\n" +
				"    \"name\": \"简单流程\"\n" +
				"  },\n" +
				"  \"processNodes\": [\n" +
				"    {\n" +
				"      \"nodeKey\": \"start\",\n" +
				"      \"name\": \"开始节点\",\n" +
				"      \"nodeType\": \"startEvent\"\n" +
				"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line1\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"start\",\n" +
				"      \"targetRef\": \"audit1\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"audit1\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"       \"auditType\": 1,\n" +
				"       \"countersign\": 1,\n" +
				"       \"users\": [\n" +
				"         10086\n" +
				"       ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line2\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit1\",\n" +
				"      \"targetRef\": \"end\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"end\",\n" +
				"      \"name\": \"结束节点\",\n" +
				"      \"nodeType\": \"endEvent\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";

		BpmnModel parse = Parse.parse(json);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(parse);

		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		byte[] bytes = bpmnXMLConverter.convertToXML(parse);
		System.out.println(new String(bytes, Charset.defaultCharset()));

		validate.forEach(validationError -> {
			System.out.println(validationError.getActivityId());
			System.out.println(validationError.getDefaultDescription());
		});
		assert validate.size() == 0;

	}


	/**
	 * 开始节点直接分支
	 */
	@Test
	public void branch() throws IOException {

		String json = "{\n" +
				"  \"process\": {\n" +
				"    \"processKey\": \"branch1\",\n" +
				"    \"name\": \"开始节点直接分支\"\n" +
				"  },\n" +
				"  \"processNodes\": [\n" +
				"    {\n" +
				"      \"nodeKey\": \"start\",\n" +
				"      \"name\": \"开始节点\",\n" +
				"      \"nodeType\": \"startEvent\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line1\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"start\",\n" +
				"      \"targetRef\": \"audit1\",\n" +
				"      \"conditionExpression\": \"1111\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"audit1\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"        \"auditType\": 1,\n" +
				"        \"countersign\": 1,\n" +
				"        \"users\": [\n" +
				"          10086\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line3\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit1\",\n" +
				"      \"targetRef\": \"end\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line2\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"start\",\n" +
				"      \"targetRef\": \"audit2\",\n" +
				"      \"conditionExpression\": \"1111\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"audit2\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"        \"auditType\": 1,\n" +
				"        \"countersign\": 1,\n" +
				"        \"users\": [\n" +
				"          10010\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line4\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit2\",\n" +
				"      \"targetRef\": \"end\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"end\",\n" +
				"      \"name\": \"结束节点\",\n" +
				"      \"nodeType\": \"endEvent\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";

		BpmnModel parse = Parse.parse(json);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(parse);

		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		byte[] bytes = bpmnXMLConverter.convertToXML(parse);
		System.out.println(new String(bytes, Charset.defaultCharset()));

		validate.forEach(validationError -> {
			System.out.println(validationError.getActivityId());
			System.out.println(validationError.getDefaultDescription());
		});
		assert validate.size() == 0;
	}

	/**
	 * 先有一个节点，再分支
	 */
	@Test
	public void branch2() throws JsonProcessingException {
		String json = "{\n" +
				"  \"process\": {\n" +
				"    \"processKey\": \"branch2\",\n" +
				"    \"name\": \"简单流程\"\n" +
				"  },\n" +
				"  \"processNodes\": [\n" +
				"    {\n" +
				"      \"nodeKey\": \"start\",\n" +
				"      \"name\": \"先有一个节点再分支\",\n" +
				"      \"nodeType\": \"startEvent\"\n" +
				"    },{\n" +
				"      \"nodeKey\": \"line5\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"start\",\n" +
				"      \"targetRef\": \"audit3\"\n" +
				"    },{\n" +
				"      \"nodeKey\": \"audit3\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"        \"auditType\": 1,\n" +
				"        \"countersign\": 1,\n" +
				"        \"users\": [\n" +
				"          10086\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line1\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit3\",\n" +
				"      \"targetRef\": \"audit1\",\n" +
				"      \"conditionExpression\": \"1111\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"audit1\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"        \"auditType\": 1,\n" +
				"        \"countersign\": 1,\n" +
				"        \"users\": [\n" +
				"          10086\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line3\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit1\",\n" +
				"      \"targetRef\": \"end\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line2\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit3\",\n" +
				"      \"targetRef\": \"audit2\",\n" +
				"      \"conditionExpression\": \"1111\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"audit2\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"        \"auditType\": 1,\n" +
				"        \"countersign\": 1,\n" +
				"        \"users\": [\n" +
				"          10010\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line4\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit2\",\n" +
				"      \"targetRef\": \"end\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"end\",\n" +
				"      \"name\": \"结束节点\",\n" +
				"      \"nodeType\": \"endEvent\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";

		BpmnModel parse = Parse.parse(json);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(parse);

		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		byte[] bytes = bpmnXMLConverter.convertToXML(parse);
		System.out.println(new String(bytes, Charset.defaultCharset()));

		validate.forEach(validationError -> {
			System.out.println(validationError.getActivityId());
			System.out.println(validationError.getDefaultDescription());
		});
		assert validate.size() == 0;
	}

	@Test
	public void test2() throws IOException {

		String userTask = "{\n" +
				"  \"process\": {\n" +
				"    \"processKey\": \"simpleFLow\",\n" +
				"    \"name\": \"简单流程\"\n" +
				"  },\n" +
				"  \"processNodes\": [\n" +
				"    {\n" +
				"      \"nodeKey\": \"start\",\n" +
				"      \"name\": \"开始节点\",\n" +
				"      \"nodeType\": \"startEvent\"\n" +
				"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line1\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"start\",\n" +
				"      \"targetRef\": \"audit1\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"audit1\",\n" +
				"      \"name\": \"审批节点1\",\n" +
				"      \"nodeType\": \"userTask\",\n" +
				"      \"approval\": {\n" +
				"       \"auditType\": 1,\n" +
				"       \"countersign\": 1,\n" +
				"       \"users\": [\n" +
				"         10086\n" +
				"       ]\n" +
				"      }\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"line2\",\n" +
				"      \"name\": \"anonymous\",\n" +
				"      \"nodeType\": \"sequenceFlow\",\n" +
				"      \"sourceRef\": \"audit1\",\n" +
				"      \"targetRef\": \"end\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"nodeKey\": \"end\",\n" +
				"      \"name\": \"结束节点\",\n" +
				"      \"nodeType\": \"endEvent\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";

		ObjectMapper objectMapper = new ObjectMapper();
		Definition definition = objectMapper.readValue(userTask, Definition.class);
		System.out.println(definition);
	}
}
