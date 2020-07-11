package com.miaojiaosan.activiti.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaojiaosan.activiti.param.Definition;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:10
 */
public class Parse {


	public static BpmnModel parse() throws JsonProcessingException {
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
			"            10086\n" +
			"          ],\n" +
			"          \"origins\":[\n" +
			"            1000010\n" +
			"          ],\n" +
			"          \"roles\": [\n" +
			"            10086\n" +
			"          ]\n" +
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

		ObjectMapper objectMapper = new ObjectMapper();
		Definition definition = objectMapper.readValue(json, Definition.class);

		Process process = definition.getProcess().create();
		definition.getProcessNodes().forEach(processNode -> {
			process.addFlowElement(processNode.create());
		});

		BpmnModel bpmnModel = new BpmnModel();
//		bpmnModel.addNamespace("bpmndi","http://www.omg.org/spec/BPMN/20100524/DI");
//		bpmnModel.addNamespace("dc","http://www.omg.org/spec/DD/20100524/DC");
//		bpmnModel.addNamespace("di","http://www.omg.org/spec/DD/20100524/DI");
//		bpmnModel.addNamespace("xsi","http://www.omg.org/spec/BPMN/20100524/DI");
//		bpmnModel.addNamespace("activiti","http://activiti.org/bpmn");
//		bpmnModel.addNamespace("camunda","http://camunda.org/schema/1.0/bpmn");
//		bpmnModel.addNamespace("tns","http://www.activiti.org/testm1574124674914");
//		bpmnModel.addNamespace("xsd","http://www.w3.org/2001/XMLSchema");
		bpmnModel.setTargetNamespace("http://www.activiti.org/testm1574124674914");
		bpmnModel.addProcess(process);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
		assert validate.size() == 0;
		return bpmnModel;
	}

	public static BpmnModel parse2() throws JsonProcessingException {
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
			"          \"countersign\": 2,\n" +
			"          \"users\":[\n" +
			"            10086,\n" +
			"            10010\n" +
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

		ObjectMapper objectMapper = new ObjectMapper();
		Definition definition = objectMapper.readValue(json, Definition.class);

		Process process = definition.getProcess().create();
		definition.getProcessNodes().forEach(processNode -> {
			process.addFlowElement(processNode.create());
		});

		BpmnModel bpmnModel = new BpmnModel();
//		bpmnModel.addNamespace("bpmndi","http://www.omg.org/spec/BPMN/20100524/DI");
//		bpmnModel.addNamespace("dc","http://www.omg.org/spec/DD/20100524/DC");
//		bpmnModel.addNamespace("di","http://www.omg.org/spec/DD/20100524/DI");
//		bpmnModel.addNamespace("xsi","http://www.omg.org/spec/BPMN/20100524/DI");
//		bpmnModel.addNamespace("activiti","http://activiti.org/bpmn");
//		bpmnModel.addNamespace("camunda","http://camunda.org/schema/1.0/bpmn");
//		bpmnModel.addNamespace("tns","http://www.activiti.org/testm1574124674914");
//		bpmnModel.addNamespace("xsd","http://www.w3.org/2001/XMLSchema");
		bpmnModel.setTargetNamespace("http://www.activiti.org/testm1574124674914");
		bpmnModel.addProcess(process);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
		assert validate.size() == 0;
		return bpmnModel;
	}
}


