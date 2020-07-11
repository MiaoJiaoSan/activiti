package com.miaojiaosan.activiti.param;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:59
 */
public class StartEvent extends Independence {

	/**
	 * 开始事件节点创建人
	 */
	Long initiator;

	public Long getInitiator() {
		return initiator;
	}

	public void setInitiator(Long initiator) {
		this.initiator = initiator;
	}

	@Override
	public org.activiti.bpmn.model.StartEvent create(ProcessNode processNode) {
		org.activiti.bpmn.model.StartEvent startEvent = new org.activiti.bpmn.model.StartEvent();
		startEvent.setId(processNode.nodeKey);
		startEvent.setName(processNode.name);
		//判断
		startEvent.setInitiator(String.valueOf(initiator));
		return startEvent;
	}
}
