package com.miaojiaosan.activiti.business;

import com.miaojiaosan.activiti.api.dto.CreateRuntimeDTO;
import com.miaojiaosan.activiti.api.dto.DeleteRuntimeDTO;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class Runtime {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private IdentityService identityService;


  public Boolean create(CreateRuntimeDTO dto) {
    try {
      Authentication.setAuthenticatedUserId(dto.getUser());
      ProcessInstance instance =
          runtimeService.startProcessInstanceByKeyAndTenantId(dto.getKey(), new HashMap<String, Object>(1) {{
            put("user", dto.getUser());
          }}, "3521");
      return Objects.nonNull(instance.getId());
    } finally {
    }
  }




  public Boolean delete(DeleteRuntimeDTO dto){
    runtimeService.deleteProcessInstance(dto.getInstanceId(), "撤回");
    return true;
  }


  @Transactional(rollbackFor = Exception.class)
  public void checkTransactional(){

    identityService.setAuthenticatedUserId("10000");

    ProcessInstance instance = runtimeService.startProcessInstanceByKey("simpleFLow", new HashMap<String, Object>() {{
    }});

    throw new RuntimeException();
  }




}
