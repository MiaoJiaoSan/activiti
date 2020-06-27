package com.miaojiaosan.activiti.business;

import com.miaojiaosan.activiti.api.dto.CreatProcessDefinitionDTO;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class Repository {

  @Autowired
  private RepositoryService repositoryService;


  public Boolean create(CreatProcessDefinitionDTO dto) {
    DeploymentBuilder deployment = repositoryService.createDeployment();
    Deployment deploy = deployment.addString(dto.getKey() + ".bpmn", dto.getXml())
        .key(dto.getKey())
        .name(dto.getName())
        .tenantId("3521")
        .category("测试")
        .deploy();
    System.out.println(deploy.getKey());
    return Objects.nonNull(deploy.getId());
  }


  public List<Map<String, Object>> list(String key) {
    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
        //最新版
        .latestVersion().processDefinitionTenantId("3521");
//    if(Objects.nonNull(key))
//      query.processDefinitionKey(key);
    return query.list().stream().map(df -> {
      HashMap<String, Object> dfMap = new HashMap<>();
      dfMap.put("id", df.getId());
      dfMap.put("key", df.getKey());
      dfMap.put("name", df.getName());
      dfMap.put("version", df.getVersion());
      return dfMap;
    }).collect(Collectors.toList());
  }


  public String detail(String key) throws IOException {
    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
        .latestVersion().processDefinitionTenantId("3521").processDefinitionKey(key);
    ProcessDefinition processDefinition = query.singleResult();
    String deploymentId = processDefinition.getDeploymentId();
    String resourceName = processDefinition.getResourceName();
    try (
      InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName)
    ){
      byte[] bytes;
      bytes = new byte[resourceAsStream.available()];
      resourceAsStream.read(bytes);
      return new String(bytes, Charset.defaultCharset());
    }
  }


}
