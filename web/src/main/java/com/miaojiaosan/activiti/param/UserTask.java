package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;

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
public class UserTask extends ProcessNode {

	Approval approval;

	@JsonIgnore
	org.activiti.bpmn.model.UserTask userTask;


	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}



	@Override
	public FlowElement create() {
		org.activiti.bpmn.model.UserTask userTask = approval.create(this);
		this.userTask = userTask;
		return userTask;
	}

	@Override
	public void setIncomingFlows(org.activiti.bpmn.model.SequenceFlow sequenceFlow){
		List<org.activiti.bpmn.model.SequenceFlow> incomingFlows = userTask.getIncomingFlows();
		Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
		userTask.setIncomingFlows(incomingFlows);
	}


	@Override
	public void setOutgoingFlows(org.activiti.bpmn.model.SequenceFlow sequenceFlow){
		List<SequenceFlow> incomingFlows = userTask.getOutgoingFlows();
		Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
		userTask.setOutgoingFlows(incomingFlows);
	}

}
