package com.miaojiaosan.activiti.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaojiaosan.activiti.param.Definition;
import com.miaojiaosan.activiti.param.ExclusiveGateway;
import com.miaojiaosan.activiti.param.ProcessNode;
import com.miaojiaosan.activiti.param.SequenceFlow;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 10:10
 */
public class Parse {


	public static BpmnModel parse(String json) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Definition definition = objectMapper.readValue(json, Definition.class);

		List<ProcessNode> processNodes = definition.getProcessNodes();
		//前期准备补全数据
		prepare(processNodes);
		String noSequenceFlowGroup = "noSequenceFlow";
		String sequenceFlowGroup = "sequenceFlow";
		//分组 一组是线 一组非线
		Map<String, List<ProcessNode>> group = processNodes.stream().collect(Collectors.groupingBy(processNode -> {
			if (processNode instanceof SequenceFlow) {
				return sequenceFlowGroup;
			} else {
				return noSequenceFlowGroup;
			}
		}));
		List<ProcessNode> groupNode = group.get(noSequenceFlowGroup);
		Map<String, ProcessNode> noSequenceFlowMap = new HashMap<>(groupNode.size());
		//先创建非线节点
		Process process = definition.getProcess().create();
		groupNode.forEach(processNode -> {
			FlowElement flowElement = processNode.create();
			noSequenceFlowMap.put(flowElement.getId(), processNode);
			process.addFlowElement(flowElement);
		});
		//再创建线节点
		groupNode = group.get(sequenceFlowGroup);
		groupNode.forEach(processNode -> {
			SequenceFlow sequenceFlow = (SequenceFlow) processNode;
			process.addFlowElement(sequenceFlow.create());
			ProcessNode source = noSequenceFlowMap.get(sequenceFlow.getSourceRef());
			ProcessNode target = noSequenceFlowMap.get(sequenceFlow.getTargetRef());
			source.setOutgoingFlows(sequenceFlow.getSequenceFlow());
			target.setIncomingFlows(sequenceFlow.getSequenceFlow());
		});

		BpmnModel bpmnModel = new BpmnModel();
		bpmnModel.setTargetNamespace("http://www.activiti.org/testm1574124674914");
		bpmnModel.addProcess(process);
		return bpmnModel;
	}

	private static void prepare(List<ProcessNode> processNodes) {
		Map<String, List<ProcessNode>> nodeGroup =
				processNodes.parallelStream().collect(Collectors.groupingBy(ProcessNode::getNodeType));

		String startEventId = nodeGroup.get("startEvent").get(0).getNodeKey();
		String endEventId = nodeGroup.get("endEvent").get(0).getNodeKey();

		List<ProcessNode> sequenceFlows = nodeGroup.get("sequenceFlow");
		//再根据 sourceRef 分组 找出所有分支
		Map<String, List<ProcessNode>> sourceMapping = sequenceFlows.stream().collect(Collectors.groupingBy(processNode -> {
			SequenceFlow sequenceFlow = (SequenceFlow) processNode;
			return sequenceFlow.getSourceRef();
		}));
		List<ProcessNode> userTasks = nodeGroup.get("userTask");
//	1、拿到所有的userTask,每个userTask 生成一个网关 与 end连线
//	2、修改每个user 的 sequenceFlow 变为 userTask-> gateway -> nextNo
//	3、特殊情况节开始条件下直接分支
		userTasks.forEach(userTask -> {

			String auditGatewayId = "exculsive"+UUID.randomUUID().toString().replace("-","");
			auditExclusiveGateway(processNodes, userTask, auditGatewayId, endEventId);

			//找到原来的连线
			List<ProcessNode> sourceSequenceFlows = sourceMapping.get(userTask.getNodeKey());
			if(sourceSequenceFlows.size() > 1){
				conditionGateway(processNodes, auditGatewayId, sourceSequenceFlows);
			}else if(sourceSequenceFlows.size() == 1){
				//如果没有分支，则直接连审批网关
				SequenceFlow conditionSequenceFlow = (SequenceFlow) sourceSequenceFlows.get(0);
				conditionSequenceFlow.setSourceRef(auditGatewayId);
				conditionSequenceFlow.setConditionExpression("${result == \"Y\"}");
			}
		});

		//特殊情况 开始节点直接分支
		List<ProcessNode> startEvents = sourceMapping.get(startEventId);
		if(startEvents.size() > 1) {
			specialCase(processNodes, startEvents, startEventId);
		}
	}

	/**
	 * 开始节点直接分支
	 */
	private static void specialCase(List<ProcessNode> processNodes, List<ProcessNode> startEvents, String startEventId) {
			ProcessNode exclusiveGateway = new ExclusiveGateway();
			String nodeKey = "exculsive"+UUID.randomUUID().toString().replace("-","");
			exclusiveGateway.setNodeKey(nodeKey);
			exclusiveGateway.setName("审批网关");
			processNodes.add(exclusiveGateway);

			ProcessNode sequenceFlow = new SequenceFlow(){{
				setNodeKey("sequenceFlow"+UUID.randomUUID().toString().replace("-",""));
				setSourceRef(startEventId);
				setTargetRef(nodeKey);
			}};
			processNodes.add(sequenceFlow);
			startEvents.forEach(processNode -> {
				SequenceFlow condition = (SequenceFlow) processNode;
				condition.setSourceRef(nodeKey);
			});
	}

	/**
	 * 条件网关
	 */
	private static void conditionGateway(List<ProcessNode> processNodes, String auditGatewayId, List<ProcessNode> sourceSequenceFlows) {
		String conditionGatewayId = "exculsive"+UUID.randomUUID().toString().replace("-","");
		ProcessNode conditionGateway = new ExclusiveGateway();
		conditionGateway.setNodeKey(conditionGatewayId);
		conditionGateway.setName("条件网关");
		processNodes.add(conditionGateway);
		//再创建一条线 一端连审批网关 一端连条件网关
		ProcessNode sequenceFlow = new SequenceFlow(){{
			setNodeKey("sequenceFlow"+UUID.randomUUID().toString().replace("-",""));
			setSourceRef(auditGatewayId);
			setTargetRef(conditionGatewayId);
			setConditionExpression("${result == \"Y\"}");
		}};
		processNodes.add(sequenceFlow);
		//修改原线路为条件网关
		sourceSequenceFlows.forEach(processNode -> {
			SequenceFlow conditionSequenceFlow = (SequenceFlow) processNode;
			conditionSequenceFlow.setSourceRef(conditionGatewayId);
		});
	}

	/**
	 * 审批网关
	 */
	private static void auditExclusiveGateway(List<ProcessNode> processNodes, ProcessNode userTask, String auditGatewayId, String endEventId) {
		ProcessNode exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setNodeKey(auditGatewayId);
		exclusiveGateway.setName("审批网关");
		processNodes.add(exclusiveGateway);
		//先创建两条线 一段 userTaskKey, 一段连end
		ProcessNode sequenceFlow = new SequenceFlow(){{
			setNodeKey("sequenceFlow"+UUID.randomUUID().toString().replace("-",""));
			setSourceRef(userTask.getNodeKey());
			setTargetRef(auditGatewayId);
		}};
		processNodes.add(sequenceFlow);
		sequenceFlow = new SequenceFlow(){{
			setNodeKey("sequenceFlow"+UUID.randomUUID().toString().replace("-",""));
			setSourceRef(auditGatewayId);
			setTargetRef(endEventId);
			setConditionExpression("${result == \"N\"}");
		}};
		processNodes.add(sequenceFlow);
	}
}


