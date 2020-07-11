package com.miaojiaosan.activiti.param;

import org.activiti.bpmn.model.ActivitiListener;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:00
 */
public class UserTask extends Independence  {

	/**
	 * 审批
	 */
	Approval approval;


	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}


	@Override
	public org.activiti.bpmn.model.UserTask create(ProcessNode processNode) {
		org.activiti.bpmn.model.UserTask userTask = new org.activiti.bpmn.model.UserTask();
		userTask.setId(processNode.getNodeKey());
		userTask.setName(processNode.getName());
//		ActivitiListener activitiListener = new ActivitiListener();
//		activitiListener.setEvent("end");
//		activitiListener.setImplementationType("delegateExpression");
//		activitiListener.setImplementation("${refuseListener}");
//		List<ActivitiListener> executionListeners = userTask.getExecutionListeners();
//		Optional.ofNullable(executionListeners)
//			.orElse(new ArrayList<>()).add(activitiListener);
		return approval.create(userTask);
	}
}
