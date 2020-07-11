package com.miaojiaosan.activiti.web;

import com.miaojiaosan.activiti.api.dto.CompleteDTO;
import com.miaojiaosan.activiti.business.MyTask;
import com.miaojiaosan.activiti.cmd.TaskComplete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/task")
public class TaskController {

  @Autowired
  private MyTask task;

  @GetMapping
  public List<Map<String, Object>> tasks(@RequestParam String assignee) throws ExecutionException, InterruptedException {
    return task.list(assignee);
  }

  @GetMapping("/{taskId}/{assignee}")
  public Boolean claim(@PathVariable String taskId, @PathVariable String assignee){
    return task.claim(taskId, assignee);
  }



  @PostMapping
  public Boolean complete(@RequestBody  TaskComplete complete){
    CompleteDTO dto = new CompleteDTO();
    BeanUtils.copyProperties(complete,dto);
    return task.complete(dto);
  }


}
