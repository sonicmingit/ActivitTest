package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class ActivitTest {
	/* 使用代码创建工作流需要的23张表 */
	@Test
	public void createTable() {
		// 创建一个单例的核心管理
		ProcessEngineConfiguration pec = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		// 连接数据库的配置
		pec.setJdbcDriver("com.mysql.jdbc.Driver");
		pec.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activititest?useUnicode=true&charsetEncoding=utf-8");
		pec.setJdbcUsername("root");
		pec.setJdbcPassword("1231");
		//

		/*
		 * public static final String DB_SCHEMA_UPDATE_FALSE = "false"; 不能自动创建表,需要表存在
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
		 * 先删除表再创建 public static final String DB_SCHEMA_UPDATE_TRUE = "true" 如果不存在,自动创建表
		 * ;
		 */
		// 设置可以自动创建表
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		// 工作流的核心对象创建
		ProcessEngine pe = pec.buildProcessEngine();
		System.out.println("创建完成:" + pe);
	}
	
	/* 使用配置文件创建工作流需要的23张表 */
	@Test
	public void createTableByXML() {
		//根据配置文件创建单例核心管理
		ProcessEngineConfiguration pec = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		//工作流核心对象创建
		ProcessEngine pe = pec.buildProcessEngine();
		System.out.println("创建完成(配置文件):" + pe);
	}
}
