package com.miaojiaosan.activiti.config;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.boot.SpringBootConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-04 1:57
 */
@SpringBootConfiguration
public class GroupEntityManagerImpl implements GroupEntityManager {

	@Override
	public Group createNewGroup(String groupId) {
		return null;
	}

	@Override
	public GroupQuery createNewGroupQuery() {
		return null;
	}

	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		return null;
	}

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
		return 0;
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		return Collections.singletonList(new GroupEntityImpl(){{
			setId("group1");
		}});
	}

	@Override
	public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
		return null;
	}

	@Override
	public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
		return 0;
	}

	@Override
	public boolean isNewGroup(Group group) {
		return false;
	}

	@Override
	public GroupEntity create() {
		return null;
	}

	@Override
	public GroupEntity findById(String entityId) {
		return null;
	}

	@Override
	public void insert(GroupEntity entity) {

	}

	@Override
	public void insert(GroupEntity entity, boolean fireCreateEvent) {

	}

	@Override
	public GroupEntity update(GroupEntity entity) {
		return null;
	}

	@Override
	public GroupEntity update(GroupEntity entity, boolean fireUpdateEvent) {
		return null;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public void delete(GroupEntity entity) {

	}

	@Override
	public void delete(GroupEntity entity, boolean fireDeleteEvent) {

	}
}
