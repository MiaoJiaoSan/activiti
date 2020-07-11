package com.miaojiaosan.activiti.param;

import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-08 11:02
 */
public class Designated extends Approval {



	Integer countersign;

	List<Long> users;
	List<Long> organizations;
	List<Long> roles;

	public Integer getCountersign() {
		return countersign;
	}

	public void setCountersign(Integer countersign) {
		this.countersign = countersign;
	}

	public List<Long> getUsers() {
		return users;
	}

	public void setUsers(List<Long> users) {
		this.users = users;
	}

	public List<Long> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Long> organizations) {
		this.organizations = organizations;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}

	@Override
	public UserTask create(UserTask userTask) {
		//角色或分组不为空不能 设置会签
		if(
			(!CollectionUtils.isEmpty(organizations)
			|| !CollectionUtils.isEmpty(roles))
			&& countersign == 2
		){
			throw new RuntimeException("选择角色或分组时不能会签");
		}
		if(countersign == 1) {
			users = Optional.ofNullable(users).orElse(Collections.emptyList());
			userTask.setCandidateUsers(users.stream().map(String::valueOf)
				.collect(Collectors.toList()));

			organizations = Optional.ofNullable(organizations).orElse(Collections.emptyList());
			userTask.setCandidateGroups(organizations.stream()
				.map(originId -> "org:" + originId).collect(Collectors.toList()));

			roles = Optional.ofNullable(roles).orElse(Collections.emptyList());
			List<String> candidateGroups = userTask.getCandidateGroups();
			candidateGroups.addAll(roles.stream()
				.map(roleId -> "role:" + roleId).collect(Collectors.toList()));

		}else if(countersign == 2){
			userTask.setAssignee("${assignee}");
			MultiInstanceLoopCharacteristics multiInstance
				= new MultiInstanceLoopCharacteristics();
			multiInstance.setLoopCardinality(String.valueOf(users.size()));
			String users = StringUtils.join(this.users.toArray(), ",");
			multiInstance.setInputDataItem("${customExpress.split(\""+users+"\")}");
			multiInstance.setElementVariable("assignee");
			//完成数等同于实例数
			multiInstance.setCompletionCondition("${nrOfInstances == nrOfCompletedInstances}");
			userTask.setLoopCharacteristics(multiInstance);
		}
		return userTask;
	}
}
