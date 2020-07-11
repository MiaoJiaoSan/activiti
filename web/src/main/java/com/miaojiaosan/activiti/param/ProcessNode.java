package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.activiti.bpmn.model.FlowElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:57
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "nodeType", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = StartEvent.class, name = "startEvent"),
		@JsonSubTypes.Type(value = UserTask.class, name = "userTask"),
		@JsonSubTypes.Type(value = EndEvent.class, name = "endEvent"),
		@JsonSubTypes.Type(value = SequenceFlow.class, name = "sequenceFlow"),
		@JsonSubTypes.Type(value = ExclusiveGateway.class, name = "exclusiveGateway")
})
public abstract class ProcessNode {


	String nodeKey;
	String name;
	/**
	 * 节点类型
	 */
	String nodeType;

	public String getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public abstract FlowElement create();

	public abstract void setIncomingFlows(org.activiti.bpmn.model.SequenceFlow sequenceFlow);


	public abstract void setOutgoingFlows(org.activiti.bpmn.model.SequenceFlow sequenceFlow);


}
