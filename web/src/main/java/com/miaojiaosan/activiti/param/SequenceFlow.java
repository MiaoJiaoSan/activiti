package com.miaojiaosan.activiti.param;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:04
 */
public class SequenceFlow extends Independence {

	String sourceRef;
	String targetRef;

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

	@Override
	public org.activiti.bpmn.model.SequenceFlow create(ProcessNode processNode) {
		org.activiti.bpmn.model.SequenceFlow sequenceFlow = new org.activiti.bpmn.model.SequenceFlow();
		sequenceFlow.setId(processNode.nodeKey);
		sequenceFlow.setName(processNode.name);
		sequenceFlow.setTargetRef(targetRef);
		sequenceFlow.setSourceRef(sourceRef);
		return sequenceFlow;
	}
}
