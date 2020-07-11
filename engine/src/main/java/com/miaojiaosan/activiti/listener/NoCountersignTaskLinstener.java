package com.miaojiaosan.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component("noCountersignTaskListener")
public class NoCountersignTaskLinstener implements TaskListener {


  @Override
  public void notify(DelegateTask delegateTask) {
    //complete
    Boolean pass = delegateTask.getVariable("pass", Boolean.class);
    if (pass) {
      //说明都完成了并且没有人拒绝
      delegateTask.setVariable("result", "Y");
    } else {
      delegateTask.setVariable("result", "N");
    }
  }

}
