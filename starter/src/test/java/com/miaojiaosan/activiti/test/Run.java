package com.miaojiaosan.activiti.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miaojiaosan.activiti.Application;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class Run {

  @Autowired
  private RepositoryService repositoryService;

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private IdentityService identityService;


  @Test
  public void simple() throws JsonProcessingException {
    String json = "{\n" +
        "  \"process\": {\n" +
        "    \"processKey\": \"simpleFLow\",\n" +
        "    \"name\": \"简单流程\"\n" +
        "  },\n" +
        "  \"processNodes\": [\n" +
        "    {\n" +
        "      \"nodeKey\": \"start\",\n" +
        "      \"name\": \"开始节点\",\n" +
        "      \"nodeType\": \"startEvent\",\n" +
        "      \"initiator\": \"10000\"\n"+
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

    Deployment deploy = repositoryService.createDeployment().addBpmnModel("simpleFLow.bpmn", parse)
        .key("simpleFLow")
        .name("简单流程")
        .deploy();

    identityService.setAuthenticatedUserId("10000");

    ProcessInstance instance = runtimeService.startProcessInstanceByKey("simpleFLow",new HashMap<String, Object>(){{
      put("startUser","10000");
    }});

    Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateUser("10086").singleResult();
    taskService.claim(task.getId(), "10086");
    //防止变量污染非网关节点
    taskService.setVariable(task.getId(),"result", null);
    taskService.setVariable(task.getId(),"pass",  false);
    taskService.complete(task.getId());
  }


  @Test
  public void simple2node() throws JsonProcessingException {
    String json = "{\n" +
        "  \"process\": {\n" +
        "    \"processKey\": \"simple2node\",\n" +
        "    \"name\": \"简单流程\"\n" +
        "  },\n" +
        "  \"processNodes\": [\n" +
        "    {\n" +
        "      \"nodeKey\": \"start\",\n" +
        "      \"name\": \"开始节点\",\n" +
        "      \"nodeType\": \"startEvent\",\n" +
        "      \"initiator\": \"10000\"\n"+
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
        "      \"targetRef\": \"audit2\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"audit2\",\n" +
        "      \"name\": \"审批节点2\",\n" +
        "      \"nodeType\": \"userTask\",\n" +
        "      \"approval\": {\n" +
        "       \"auditType\": 1,\n" +
        "       \"countersign\": 1,\n" +
        "       \"users\": [\n" +
        "         10010\n" +
        "       ]\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line3\",\n" +
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

    Deployment deploy = repositoryService.createDeployment().addBpmnModel("simple2node.bpmn", parse)
        .key("simple2node")
        .name("简单流程")
        .deploy();

    identityService.setAuthenticatedUserId("10000");

    ProcessInstance instance = runtimeService.startProcessInstanceByKey("simple2node",new HashMap<String, Object>(){{
      put("startUser","10000");
    }});

    Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateUser("10086").singleResult();
    taskService.claim(task.getId(), "10086");
    //防止变量污染非网关节点
    taskService.setVariable(task.getId(),"result", null);
    taskService.setVariable(task.getId(),"pass",  false);
    taskService.complete(task.getId());
  }


  @Test
  public void countersign() throws JsonProcessingException {
    String json = "{\n" +
        "  \"process\": {\n" +
        "    \"processKey\": \"countersign\",\n" +
        "    \"name\": \"简单流程\"\n" +
        "  },\n" +
        "  \"processNodes\": [\n" +
        "    {\n" +
        "      \"nodeKey\": \"start\",\n" +
        "      \"name\": \"开始节点\",\n" +
        "      \"nodeType\": \"startEvent\",\n" +
        "      \"initiator\": \"10000\"\n"+
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
        "       \"countersign\": 2,\n" +
        "       \"users\": [\n" +
        "         10086,10010\n" +
        "       ]\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line2\",\n" +
        "      \"name\": \"anonymous\",\n" +
        "      \"nodeType\": \"sequenceFlow\",\n" +
        "      \"sourceRef\": \"audit1\",\n" +
        "      \"targetRef\": \"audit2\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"audit2\",\n" +
        "      \"name\": \"审批节点2\",\n" +
        "      \"nodeType\": \"userTask\",\n" +
        "      \"approval\": {\n" +
        "       \"auditType\": 1,\n" +
        "       \"countersign\": 1,\n" +
        "       \"users\": [\n" +
        "         10010\n" +
        "       ]\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line3\",\n" +
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

    Deployment deploy = repositoryService.createDeployment().addBpmnModel("countersign.bpmn", parse)
        .key("countersign")
        .name("简单流程")
        .deploy();

    identityService.setAuthenticatedUserId("10000");

    ProcessInstance instance = runtimeService.startProcessInstanceByKey("countersign",new HashMap<String, Object>(){{
      put("startUser","10000");
    }});

    Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateOrAssigned("10086").singleResult();
    taskService.claim(task.getId(), "10086");
    //防止变量污染非网关节点
    taskService.setVariable(task.getId(),"result", null);
    taskService.setVariable(task.getId(),"pass",false);
    taskService.complete(task.getId());
  }

  @Test
  public void countersignAgree() throws JsonProcessingException {
    String json = "{\n" +
        "  \"process\": {\n" +
        "    \"processKey\": \"countersignAgree\",\n" +
        "    \"name\": \"简单流程\"\n" +
        "  },\n" +
        "  \"processNodes\": [\n" +
        "    {\n" +
        "      \"nodeKey\": \"start\",\n" +
        "      \"name\": \"开始节点\",\n" +
        "      \"nodeType\": \"startEvent\",\n" +
        "      \"initiator\": \"10000\"\n"+
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
        "       \"countersign\": 2,\n" +
        "       \"users\": [\n" +
        "         10086,10010\n" +
        "       ]\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line2\",\n" +
        "      \"name\": \"anonymous\",\n" +
        "      \"nodeType\": \"sequenceFlow\",\n" +
        "      \"sourceRef\": \"audit1\",\n" +
        "      \"targetRef\": \"audit2\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"audit2\",\n" +
        "      \"name\": \"审批节点2\",\n" +
        "      \"nodeType\": \"userTask\",\n" +
        "      \"approval\": {\n" +
        "       \"auditType\": 1,\n" +
        "       \"countersign\": 1,\n" +
        "       \"users\": [\n" +
        "         1008611\n" +
        "       ]\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line3\",\n" +
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

    Deployment deploy = repositoryService.createDeployment().addBpmnModel("countersignAgree.bpmn", parse)
        .key("countersignAgree")
        .name("简单流程")
        .deploy();

    identityService.setAuthenticatedUserId("10000");

    ProcessInstance instance = runtimeService.startProcessInstanceByKey("countersignAgree",new HashMap<String, Object>(){{
      put("startUser","10000");
    }});
    //audit1
    Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateOrAssigned("10086").singleResult();
    taskService.claim(task.getId(), "10086");
    //防止变量污染非网关节点
    taskService.setVariable(task.getId(),"result", null);
    taskService.setVariable(task.getId(),"pass",true);
    taskService.complete(task.getId());

    task = taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateOrAssigned("10010").singleResult();
    taskService.claim(task.getId(), "10010");
    //防止变量污染非网关节点
    taskService.setVariable(task.getId(),"result", null);
    taskService.setVariable(task.getId(),"pass",true);
    taskService.complete(task.getId());
    //audit2
    task = taskService.createTaskQuery().processInstanceId(instance.getId()).taskCandidateOrAssigned("1008611").singleResult();
    taskService.claim(task.getId(), "1008611");
    //防止变量污染非网关节点
    taskService.setVariable(task.getId(),"result", null);
    taskService.setVariable(task.getId(),"pass",true);
    taskService.complete(task.getId());
  }

  @Test
  public void branch() throws JsonProcessingException {
    String json = "{\n" +
        "  \"process\": {\n" +
        "    \"processKey\": \"branch\",\n" +
        "    \"name\": \"简单流程\"\n" +
        "  },\n" +
        "  \"processNodes\": [\n" +
        "    {\n" +
        "      \"nodeKey\": \"start\",\n" +
        "      \"name\": \"开始节点\",\n" +
        "      \"nodeType\": \"startEvent\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line3\",\n" +
        "      \"name\": \"anonymous\",\n" +
        "      \"nodeType\": \"sequenceFlow\",\n" +
        "      \"sourceRef\": \"start\",\n" +
        "      \"targetRef\": \"audit2\",\n" +
        "      \"order\": 1,"+
        "      \"conditionExpression\": \"${amount > 1000}\""+
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line1\",\n" +
        "      \"name\": \"anonymous\",\n" +
        "      \"nodeType\": \"sequenceFlow\",\n" +
        "      \"sourceRef\": \"start\",\n" +
        "      \"targetRef\": \"audit1\",\n" +
        "      \"conditionExpression\": \"${amount > 500}\""+
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
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"audit2\",\n" +
        "      \"name\": \"审批节点2\",\n" +
        "      \"nodeType\": \"userTask\",\n" +
        "      \"approval\": {\n" +
        "       \"auditType\": 1,\n" +
        "       \"countersign\": 1,\n" +
        "       \"users\": [\n" +
        "         10010\n" +
        "       ]\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"nodeKey\": \"line4\",\n" +
        "      \"name\": \"anonymous\",\n" +
        "      \"nodeType\": \"sequenceFlow\",\n" +
        "      \"sourceRef\": \"audit2\",\n" +
        "      \"targetRef\": \"end\"\n" +
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

    Deployment deploy = repositoryService.createDeployment().addBpmnModel("branch.bpmn", parse)
        .key("branch")
        .name("简单流程")
        .deploy();

    identityService.setAuthenticatedUserId("10000");

    ProcessInstance instance = runtimeService.startProcessInstanceByKey("branch",new HashMap<String, Object>(){{
      put("startUser","10000");
      put("amount","100000");
    }});

  }

}
