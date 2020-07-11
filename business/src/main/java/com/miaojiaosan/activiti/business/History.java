package com.miaojiaosan.activiti.business;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-06 11:35
 */
@Service
public class History {

	@Autowired
	HistoryService historyService;

	public Boolean actProcessDefinition(String assignee){
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		query.startedBy(assignee);
		List<HistoricProcessInstance> list = query.list();
		return true;
	}

}
