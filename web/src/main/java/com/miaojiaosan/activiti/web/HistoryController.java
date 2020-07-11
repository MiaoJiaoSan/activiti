package com.miaojiaosan.activiti.web;

import com.miaojiaosan.activiti.business.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-06 10:55
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	History history;

	@GetMapping("/{assignee}")
	public void actProcessDefinition(@PathVariable String assignee){
		history.actProcessDefinition(assignee);
	}

}

