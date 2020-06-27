package com.miaojiaosan.activiti.web;

import com.miaojiaosan.activiti.api.dto.CreateRuntimeDTO;
import com.miaojiaosan.activiti.api.dto.DeleteRuntimeDTO;
import com.miaojiaosan.activiti.business.Runtime;
import com.miaojiaosan.activiti.cmd.CreateRuntime;
import com.miaojiaosan.activiti.cmd.DeleteRuntime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/runtime")
public class RuntimeController {

  @Autowired
  private Runtime runtime;

  @PostMapping
  public Boolean runtime(@RequestBody CreateRuntime create){
    CreateRuntimeDTO dto = new CreateRuntimeDTO();
    BeanUtils.copyProperties(create, dto);
    return runtime.create(dto);
  }


  @DeleteMapping
  public Boolean runtime(@RequestBody DeleteRuntime delete){
    DeleteRuntimeDTO dto = new DeleteRuntimeDTO();
    BeanUtils.copyProperties(delete,dto);
    return runtime.delete(dto);
  }
}
