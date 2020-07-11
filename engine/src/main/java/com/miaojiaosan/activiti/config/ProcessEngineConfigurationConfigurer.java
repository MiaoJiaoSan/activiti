package com.miaojiaosan.activiti.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-04 1:15
 */
@SpringBootConfiguration
public class ProcessEngineConfigurationConfigurer implements org.activiti.spring.boot.ProcessEngineConfigurationConfigurer {
	@Override
	public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
		processEngineConfiguration.setGroupEntityManager(new GroupEntityManagerImpl());
		processEngineConfiguration.setUserEntityManager(new UserEntityManagerImpl());
	}



	@Component("customExpress")
	public static class CustomExpress{

		@Resource
		private RemoteService remoteService;

		public List<String> split(String src){
			remoteService.remote();
			String[] split = src.split(",");
			return Arrays.asList(split);
		}

		public String leader(String startUser, Integer level, Long whenOrgNotExists){

			return String.valueOf(whenOrgNotExists);
		}

		@PostConstruct
		public void init(){
			System.out.println("init");
		}

	}
}
