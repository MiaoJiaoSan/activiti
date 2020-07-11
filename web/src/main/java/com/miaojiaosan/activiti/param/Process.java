package com.miaojiaosan.activiti.param;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:56
 */
public class Process {

	String processKey;
	String name;

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public org.activiti.bpmn.model.Process create() {
		org.activiti.bpmn.model.Process process = new org.activiti.bpmn.model.Process();
		process.setId(processKey);
		process.setName(name);
		process.setExecutable(true);
		return process;
	}
}
