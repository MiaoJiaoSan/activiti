package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * create by 2020-07-08 10:59
 */
public class EndEvent extends ProcessNode {


	@JsonIgnore
	org.activiti.bpmn.model.EndEvent endEvent;

	@Override
	public org.activiti.bpmn.model.EndEvent create() {
		org.activiti.bpmn.model.EndEvent endEvent = new org.activiti.bpmn.model.EndEvent();
		endEvent.setId(nodeKey);
		endEvent.setName(name);
		this.endEvent = endEvent;
		return endEvent;
	}

	@Override
	public void setIncomingFlows(SequenceFlow sequenceFlow) {
		List<SequenceFlow> incomingFlows = endEvent.getIncomingFlows();
		Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
		endEvent.setIncomingFlows(incomingFlows);
	}

	@Override
	public void setOutgoingFlows(SequenceFlow sequenceFlow) {
		List<SequenceFlow> incomingFlows = endEvent.getOutgoingFlows();
		Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
		endEvent.setOutgoingFlows(incomingFlows);
	}
}
