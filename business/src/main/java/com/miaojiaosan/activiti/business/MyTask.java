package com.miaojiaosan.activiti.business;

import com.miaojiaosan.activiti.api.dto.CompleteDTO;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MyTask {

  @Autowired
  private TaskService task;

  public List<Map<String, Object>> list(String assignee) throws ExecutionException, InterruptedException {
    return CompletableFuture.supplyAsync(() -> {
      TaskQuery taskQuery = task.createTaskQuery();
      taskQuery.taskTenantId("3521");
      taskQuery.taskAssignee(assignee);
      List<Task> list = taskQuery.list();
      return list.stream().map(task -> new HashMap<String, Object>(4) {{
        put("id", task.getId());
        put("processInstanceId", task.getProcessInstanceId());
        put("name", task.getName());
        put("executionId", task.getExecutionId());
      }}).collect(Collectors.toList());
    }).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
      TaskQuery taskQuery = task.createTaskQuery();
      taskQuery.taskTenantId("3521");
      taskQuery.taskCandidateUser(assignee);
      List<Task> list = taskQuery.list();
      return list.stream().map(task -> new HashMap<String, Object>(4) {{
        put("id", task.getId());
        put("processInstanceId", task.getProcessInstanceId());
        put("name", task.getName());
        put("executionId", task.getExecutionId());
      }}).collect(Collectors.toList());
    }), (list1, list2) -> new ArrayList<Map<String, Object>>(2){{
        addAll(list1);
        addAll(list2);
      }}
    ).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
      TaskQuery taskQuery = task.createTaskQuery();
      taskQuery.taskTenantId("3521");
      taskQuery.taskCandidateGroup(assignee);
      List<Task> list = taskQuery.list();
      return list.stream().map(task -> new HashMap<String, Object>(4) {{
        put("id", task.getId());
        put("processInstanceId", task.getProcessInstanceId());
        put("name", task.getName());
        put("executionId", task.getExecutionId());
      }}).collect(Collectors.toList());
    }), (list1, list2) -> new ArrayList<Map<String, Object>>(2){{
        addAll(list1);
        addAll(list2);
      }}
    ).get();
  }


  public Boolean complete(CompleteDTO dto){
    task.setAssignee(dto.getTaskId(),dto.getAssignee());
    task.complete(dto.getTaskId(),new HashMap<String, Object>(1) {{
      put("users", "lisi,wangwu,zhaoliu");
    }});
    return true;
  }

}
