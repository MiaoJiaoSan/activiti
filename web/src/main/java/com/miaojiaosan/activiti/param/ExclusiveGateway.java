package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExclusiveGateway extends ProcessNode {

  @JsonIgnore
  org.activiti.bpmn.model.ExclusiveGateway exclusiveGateway;

  @Override
  public FlowElement create() {

    org.activiti.bpmn.model.ExclusiveGateway exclusiveGateway = new org.activiti.bpmn.model.ExclusiveGateway();
    exclusiveGateway.setId(nodeKey);
    exclusiveGateway.setName(name);
    this.exclusiveGateway = exclusiveGateway;
    return exclusiveGateway;
  }

  @Override
  public void setIncomingFlows(SequenceFlow sequenceFlow) {
    List<SequenceFlow> incomingFlows = exclusiveGateway.getIncomingFlows();
    Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
    exclusiveGateway.setIncomingFlows(incomingFlows);
  }

  @Override
  public void setOutgoingFlows(SequenceFlow sequenceFlow) {
    List<SequenceFlow> incomingFlows = exclusiveGateway.getOutgoingFlows();
    Optional.of(incomingFlows).orElse(new ArrayList<>()).add(sequenceFlow);
    exclusiveGateway.setOutgoingFlows(incomingFlows);
  }
}
