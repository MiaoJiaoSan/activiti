package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.activiti.bpmn.model.UserTask;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "auditType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Designated.class, name = "1"),
    @JsonSubTypes.Type(value = OrganizationManager.class, name = "2"),
    @JsonSubTypes.Type(value = FromData.class, name = "3"),
})
public abstract class Approval {

  /**
   * 审批类型
   */
  Integer auditType;

  public Integer getAuditType() {
    return auditType;
  }

  public void setAuditType(Integer auditType) {
    this.auditType = auditType;
  }

  public UserTask create(ProcessNode processNode){
    org.activiti.bpmn.model.UserTask userTask = new UserTask();
    userTask.setId(processNode.nodeKey);
    userTask.setName(processNode.name);
    return create(userTask);
  }

  public abstract org.activiti.bpmn.model.UserTask create(UserTask userTask);
}
