package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.activiti.bpmn.model.FlowElement;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:58
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "nodeType", visible = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = StartEvent.class, name = "startEvent"),
	@JsonSubTypes.Type(value = UserTask.class, name = "userTask"),
	@JsonSubTypes.Type(value = EndEvent.class, name = "endEvent"),
	@JsonSubTypes.Type(value = SequenceFlow.class, name = "sequenceFlow")
})
public abstract class Independence {
	/**
	 * 节点类型
	 */
	String nodeType;

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public abstract FlowElement create(ProcessNode processNode);

}
