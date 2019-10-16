package com.bd.sparkproject;

import com.bd.sparkproject.conf.ConfigurationManager;

/**
 * 配置管理组件测试类
 * @author Administrator
 *
 */
public class ConfigurationManagerTest {

	public static void main(String[] args) {
		String testkey1 = ConfigurationManager.getProperty("kafka.metadata.broker.list");
		String testkey2 = ConfigurationManager.getProperty("kafka.topics");
		System.out.println(testkey1);  
		System.out.println(testkey2);  
	}
	
}
