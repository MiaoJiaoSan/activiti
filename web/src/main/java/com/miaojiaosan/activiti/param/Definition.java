package com.miaojiaosan.activiti.param;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:11
 */
public class Definition {

	Process process;

	List<ProcessNode> processNodes;

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public List<ProcessNode> getProcessNodes() {
		return processNodes;
	}

	public void setProcessNodes(List<ProcessNode> processNodes) {
		this.processNodes = processNodes;
	}
}
