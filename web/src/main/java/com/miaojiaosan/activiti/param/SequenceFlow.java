package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:04
 */
public class SequenceFlow extends ProcessNode {

	String sourceRef;
	String targetRef;
	String conditionExpression;
	@JsonIgnore
	org.activiti.bpmn.model.SequenceFlow sequenceFlow;

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public org.activiti.bpmn.model.SequenceFlow getSequenceFlow() {
		return sequenceFlow;
	}

	public void setSequenceFlow(org.activiti.bpmn.model.SequenceFlow sequenceFlow) {
		this.sequenceFlow = sequenceFlow;
	}

	@Override
	public org.activiti.bpmn.model.SequenceFlow create() {
		org.activiti.bpmn.model.SequenceFlow sequenceFlow = new org.activiti.bpmn.model.SequenceFlow();
		sequenceFlow.setId(nodeKey);
		sequenceFlow.setName(name);
		sequenceFlow.setTargetRef(targetRef);
		sequenceFlow.setSourceRef(sourceRef);
		sequenceFlow.setConditionExpression(conditionExpression);
		this.sequenceFlow = sequenceFlow;
		return sequenceFlow;
	}

	@Override
	public void setIncomingFlows(org.activiti.bpmn.model.SequenceFlow sequenceFlow) {

	}

	@Override
	public void setOutgoingFlows(org.activiti.bpmn.model.SequenceFlow sequenceFlow) {

	}
}
