package com.miaojiaosan.activiti.business;

import com.miaojiaosan.activiti.api.dto.CompleteDTO;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MyTask {

  @Autowired
  private TaskService task;

  public List<Map<String, Object>> list(String assignee) throws ExecutionException, InterruptedException {
    TaskQuery taskQuery = task.createTaskQuery();
    taskQuery.taskTenantId("3521");

    taskQuery.taskCandidateUser(assignee);
//    group and groups is null call UserGroupManager
//    candidateUser is not null 获取组
//    if candidateUser is null
//    userIdForCandidateAndAssignee is not null 获取组
//    taskQuery.taskCandidateGroup(assignee);
    List<Task> list = taskQuery.list();

    return list.stream().map(task -> new HashMap<String, Object>(4) {{
      put("id", task.getId());
      put("processInstanceId", task.getProcessInstanceId());
      put("name", task.getName());
      put("executionId", task.getExecutionId());
    }}).collect(Collectors.toList());
  }

  public Boolean claim(String taskId,String assignee){
    task.claim(taskId, assignee);
    return true;
  }


  public Boolean complete(CompleteDTO dto){
    task.setAssignee(dto.getTaskId(),dto.getAssignee());
    task.complete(dto.getTaskId()/*,new HashMap<String, Object>(1) {{
      put("day", 4);
      put("assigneeList", Arrays.asList("lisi","wangwu","zhaoliu"));
    }}*/);
    return true;
  }

}
