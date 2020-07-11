package com.miaojiaosan.activiti.param;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:59
 */
public class EndEvent extends Independence {


	@Override
	public org.activiti.bpmn.model.EndEvent create(ProcessNode processNode) {
		org.activiti.bpmn.model.EndEvent endEvent = new org.activiti.bpmn.model.EndEvent();
		endEvent.setId(processNode.nodeKey);
		endEvent.setName(processNode.name);
		return endEvent;
	}
}
