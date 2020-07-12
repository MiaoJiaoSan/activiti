package com.miaojiaosan.activiti.listener;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("countersignTaskListener")
public class CountersignTaskListener implements TaskListener {

  @Autowired
  TaskService taskService;


  @Override
  public void notify(DelegateTask delegateTask) {
    //complete
    String id = delegateTask.getId();
    Boolean pass = taskService.getVariable(id,"pass", Boolean.class);
    if(pass){
      Integer complete =  taskService.getVariable(id,"nrOfCompletedInstances",Integer.class);
      Integer all =  taskService.getVariable( id,"nrOfInstances",Integer.class);
      //说明都完成了并且没有人拒绝
      if (all == null || (complete + 1) == all) {
        taskService.setVariable(id,"result", "Y");
      }
    }else{
      taskService.setVariable(id,"result","N");
    }
  }
}
