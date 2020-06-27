package com.miaojiaosan.activiti.web;

import com.miaojiaosan.activiti.api.dto.CreatProcessDefinitionDTO;
import com.miaojiaosan.activiti.business.Repository;
import com.miaojiaosan.activiti.cmd.CreatProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repository")
public class RepositoryController {

  @Autowired
  private Repository repository;

  @PostMapping
  public Boolean processDefinition(@RequestBody CreatProcessDefinition create){
    CreatProcessDefinitionDTO dto = new CreatProcessDefinitionDTO();
    BeanUtils.copyProperties(create, dto);
    return repository.create(dto);
  }


  @GetMapping
  public List<?> processDefinition(@RequestParam String key){
    return repository.list(key);
  }


  @GetMapping("/{key}")
  public Map<String,String> processDefinitionSingle(@PathVariable String key) throws IOException {
    return new HashMap<String, String>(1){{
      put("xml",repository.detail(key));
    }};
  }


}
