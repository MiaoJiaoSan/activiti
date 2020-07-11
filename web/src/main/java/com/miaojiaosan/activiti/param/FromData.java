package com.miaojiaosan.activiti.param;

import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.UserTask;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:03
 */
public class FromData extends Approval {

	/**
	 * 数据中的userId字段key
	 */
	Long approverIdKey;

	public Long getApproverIdKey() {
		return approverIdKey;
	}

	public void setApproverIdKey(Long approverIdKey) {
		this.approverIdKey = approverIdKey;
	}

	@Override
	public UserTask create(UserTask userTask) {
		userTask.setAssignee("${assignee}");
		userTask.addAttribute(new ExtensionAttribute("activiti:approverIdKey"){{
			setValue(String.valueOf(approverIdKey));
		}});
		return userTask;
	}
}
