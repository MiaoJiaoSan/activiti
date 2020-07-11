package com.miaojiaosan.activiti.business;

import com.miaojiaosan.activiti.api.dto.CreateRuntimeDTO;
import com.miaojiaosan.activiti.api.dto.DeleteRuntimeDTO;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class Runtime {

  @Autowired
  private RuntimeService runtimeService;

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

}
