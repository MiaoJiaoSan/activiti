package com.miaojiaosan.activiti.param;

import org.activiti.bpmn.model.FlowElement;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:57
 */
public class ProcessNode {
	String nodeKey;
	String name;
	/**
	 * 独立属性
	 */
	Independence independence;

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

	public Independence getIndependence() {
		return independence;
	}

	public void setIndependence(Independence independence) {
		this.independence = independence;
	}

	public FlowElement create(){
		return independence.create(this);
	}


}
