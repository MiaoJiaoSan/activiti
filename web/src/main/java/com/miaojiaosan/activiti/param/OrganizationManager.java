package com.miaojiaosan.activiti.param;

import org.activiti.bpmn.model.UserTask;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:03
 */
public class OrganizationManager extends Approval {

	/**
	 * 层级
	 */
	Integer level;
	/**
	 * 当组织不存在时 审批人
	 */
	Long whenOrgNotExists;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getWhenOrgNotExists() {
		return whenOrgNotExists;
	}

	public void setWhenOrgNotExists(Long whenOrgNotExists) {
		this.whenOrgNotExists = whenOrgNotExists;
	}

	@Override
	public UserTask create(UserTask userTask) {
		userTask.setAssignee(
			"${customExpress.leader(startUser, " + this.level + "," + this.whenOrgNotExists + ")}"
		);
		return userTask;
	}
}
