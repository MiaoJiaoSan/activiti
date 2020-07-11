package com.miaojiaosan.activiti.config;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.boot.SpringBootConfiguration;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-07 13:57
 */
@SpringBootConfiguration
public class UserEntityManagerImpl implements UserEntityManager {

	@Override
	public User createNewUser(String userId) {
		return null;
	}

	@Override
	public void updateUser(User updatedUser) {

	}

	@Override
	public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
		return null;
	}

	@Override
	public long findUserCountByQueryCriteria(UserQueryImpl query) {
		return 0;
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		return null;
	}

	@Override
	public UserQuery createNewUserQuery() {
		return null;
	}

	@Override
	public Boolean checkPassword(String userId, String password) {
		return null;
	}

	@Override
	public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
		return null;
	}

	@Override
	public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
		return 0;
	}

	@Override
	public boolean isNewUser(User user) {
		return false;
	}

	@Override
	public Picture getUserPicture(String userId) {
		return null;
	}

	@Override
	public void setUserPicture(String userId, Picture picture) {

	}

	@Override
	public void deletePicture(User user) {

	}

	@Override
	public UserEntity create() {
		return null;
	}

	@Override
	public UserEntity findById(String entityId) {
		return null;
	}

	@Override
	public void insert(UserEntity entity) {

	}

	@Override
	public void insert(UserEntity entity, boolean fireCreateEvent) {

	}

	@Override
	public UserEntity update(UserEntity entity) {
		return null;
	}

	@Override
	public UserEntity update(UserEntity entity, boolean fireUpdateEvent) {
		return null;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public void delete(UserEntity entity) {

	}

	@Override
	public void delete(UserEntity entity, boolean fireDeleteEvent) {

	}
}
