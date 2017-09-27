package win.sonic.d_processVariables;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.sun.jmx.snmp.tasks.TaskServer;

/**
 * 流程变量
 */
public class ProcessVariables {
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
	// 与流程实例,执行对象(正在执行)相关
	RuntimeService run = pe.getRuntimeService();
	// 与任务(正在执行)相关
	TaskService task = pe.getTaskService();

	/** 1.部署流程定义(从inputstream) */
	@Test
	public void deploymentProcessDefinition_inputstream() {
		InputStream bpmn = this.getClass().getResourceAsStream("/diagrams/var.bpmn");
		InputStream png = this.getClass().getResourceAsStream("/diagrams/var.png");
		System.out.println("----------------------1.部署流程定义config-------------------");
		Deployment dep = pe.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("流程变量")// 添加部署名称
				.addInputStream("var.bpmn", bpmn)// 使用资源文件名(要求:与资源问文件一致)
				.addInputStream("var.png", png)// 使用资源文件名(要求:与资源问文件一致)
				.deploy();// 完成部署
		System.out.println("部署ID:" + dep.getId());
		System.out.println("部署名称:" + dep.getName());

	}

	/** 2.启动流程实例 */
	@Test
	public void startProcessInstance() {
		System.out.println("---------------------2.启动流程实例--------------------");
		String processDefinitionKey = "var";// 流程id Process-id
		ProcessInstance p1 = pe.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
				.startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例,key对应.bpmn文件Process的id,使用key值启动,默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID:" + p1.getId());// 流程实例ID
		System.out.println("流程定义ID:" + p1.getProcessDefinitionId());// 流程定义ID

	}

	/** 设置流程变量 act_ru_variable */
	@Test
	public void setVariables() {

		String taskId = "85002";
		task.setVariable(taskId, "请假天数", 5);
		// task.setVariable(taskId, "请假日期", new Date());
		task.setVariable(taskId, "请假原因", "测试请假流程");

		System.out.println("设置成功!");
	}

	/** 获取流程变量 act_ru_variable */
	@Test
	public void getVariable() {
		String taskId = "85002";
		int days = (Integer) task.getVariable(taskId, "请假天数");
		Date time = (Date) task.getVariable(taskId, "请假日期");
		String resean = (String) task.getVariable(taskId, "请假原因");
		System.out.println("请假天数:" + days + " 请假日期:" + time + " 请假原因:" + resean);

	}

	/** 设置流程变量(使用JavaBean) act_ru_variable */
	@Test
	public void setVariables_bean() {
		String taskId = "92505";
		/** 当一个javabean(实现序列化)放置到流程变量中,要求javabean属性不能再变化,如果发生变化,在获取时,抛出异常 */
		/**
		 * 解决方案:在javabean对象中添加一个固定编号<br/>
		 * private static final long serialVersionUID = -6861432963497907648L;
		 */
		Var v = new Var();
		v.setDay(12);
		v.setName("测试对象");
		v.setReason("就是个测试!!!");
		task.setVariable(taskId, "人员信息", v);// bean必须序列化才能被添加,且放入act_ge_bytearray表中
		System.out.println("bean设置变量成功!");

	}

	/** 获取流程变量(获取JavaBean) act_ru_variable */
	@Test
	public void getVariable_bean() {
		String taskId = "92505";
		Var v = (Var) task.getVariable(taskId, "人员信息");
		System.out.println(v);
	}

	/** 模拟设置和获取流程变量的场景 */
	public void setAndGetVariable() {
		/** 设置流程变量 */
		// run.setVariable(executionId, variableName, value);
		// 表示使用[执行对象]id,和流程变量的名称来设置流程变量的值(一次只能设置一个)
		// run.setVariables(executionId, variables);//
		// 表示使用[执行对象]id,和map集合来设置变量,key是流程变量名称,value是变量值
		// run.setVariableLocal(executionId, variableName,value);
		// 同上,只是只与当前executionId任务绑定(历史表会有绑定id),完成就失效
		// task.setVariable(taskId, variableName, value);//
		// 表示使用[任务]id,和流程变量的名称来设置流程变量的值(一次只能设置一个)
		// run.startProcessInstanceByKey(processDefinitionKey,variables);//
		// 启用流程实例的同时,可以设置流程变量,用map
		// task.complete(taskId, variables);//完成任务的同时,可以设置流程变量,用map
		/** 获取流程变量 */
		// run.getVariable(executionId, variableName);// 使用执行对象id和流程变量的名称获取流程变量的值
		// run.getVariables(executionId);// 使用执行对象id获取所有的流程变量,返回map集合
		// run.getVariables(executionId, variableNames);//
		// 使用执行对象id,获取流程变量的名称,通过list集合获取指定流程变量名称的值,返回map
		// task.getVariable(taskId, variableName);// 使用[任务]id和流程变量的名称获取流程变量的值,其他方法一样
	}

	/**
	 * 2.完成流程任务 act_ru_task
	 */
	@Test
	public void completeMyTask() {
		System.out.println("------------------完成我的任务-----------------------");
		String taskId = "85002";// 任务ID,创建任务时生成的act_ru_task表中id
		pe.getTaskService()// 与正在执行的任务管理相关的Service
				.complete(taskId);// 完成任务id
		System.out.println("完成任务,id:" + taskId);
	}

	/**
	 * 查询流程变量的历史表 act_hi_varinst
	 */
	@Test
	public void findHistoryProcessVariable() {
		List<HistoricVariableInstance> list = pe.getHistoryService()//
				.createHistoricVariableInstanceQuery()// 创建一个历史的流程变量实例
				.variableName("请假天数")// 根据变量名称查询
				.list();

		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance his : list) {
				System.out.println(his.getValue());
			}
		}
	}
}
