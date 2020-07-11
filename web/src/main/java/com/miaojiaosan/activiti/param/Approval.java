package com.miaojiaosan.activiti.param;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.activiti.bpmn.model.UserTask;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:01
 */
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

	public abstract UserTask create(org.activiti.bpmn.model.UserTask userTask);
}
