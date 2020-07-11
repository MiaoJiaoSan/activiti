package com.miaojiaosan.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component("countersignTaskListener")
public class CountersignTaskListener implements TaskListener {


  @Override
  public void notify(DelegateTask delegateTask) {
    //complete
    Boolean pass = delegateTask.getVariable("pass", Boolean.class);
    if(pass){
      Integer complete =  delegateTask.getVariable("nrOfCompletedInstances",Integer.class);
      Integer all =  delegateTask.getVariable( "nrOfInstances",Integer.class);
      //说明都完成了并且没有人拒绝
      if (all == null || (complete + 1) == all) {
        delegateTask.setVariable("result", "Y");
      }
      delegateTask.setVariable("pass",null);
    }else{
      delegateTask.setVariable("result","N");
    }
  }
}
