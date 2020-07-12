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
public class StartEvent extends ProcessNode {



	@JsonIgnore
	org.activiti.bpmn.model.StartEvent startEvent;



	@Override
	public org.activiti.bpmn.model.StartEvent create() {
		org.activiti.bpmn.model.StartEvent startEvent = new org.activiti.bpmn.model.StartEvent();
		startEvent.setId(nodeKey);
		startEvent.setName(name);
		//判断
		startEvent.setInitiator("startUser");
		this.startEvent = startEvent;
		return startEvent;
	}

	@Override
	public void setIncomingFlows(SequenceFlow sequenceFlow) {
		List<SequenceFlow> incomingFlows = startEvent.getIncomingFlows();
		Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
		startEvent.setIncomingFlows(incomingFlows);
	}

	@Override
	public void setOutgoingFlows(SequenceFlow sequenceFlow) {
		List<SequenceFlow> incomingFlows = startEvent.getOutgoingFlows();
		Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
		startEvent.setOutgoingFlows(incomingFlows);
	}
}
