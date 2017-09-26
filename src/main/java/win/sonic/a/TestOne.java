package win.sonic.a;

import java.util.Iterator;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestOne {
	// 创建一个核心引擎
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义 RepositoryService <br/>
	 * 对应表 act_re_deployment act_re_procdef act_ru_execution
	 */
	@Test
	public void deploymentProcessDefinition() {
		String diagram = "diagrams/test";// 流程路径
		String bpmn = diagram + ".bpmn";// bpmn文件
		String png = diagram + ".png";// png文件
		System.out.println("----------------------部署流程定义-------------------");
		Deployment dep = pe.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("入门测试工作流")//添加部署名称
				.addClasspathResource(bpmn)// 从classpath的资源中加载,一次只能加载一个
				.addClasspathResource(png)// 从classpath的资源中加载,一次只能加载一个
				.deploy();// 完成部署
		System.out.println("部署ID:" + dep.getId());
		System.out.println("部署名称:" + dep.getName());

	}

	/**
	 * 启动流程实例 RuntimeService
	 */
	@Test
	public void startProcessInstance() {
		System.out.println("---------------------启动流程实例--------------------");
		String processDefinitionKey = "ask";// 流程id Process-id
		ProcessInstance p1 = pe.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
				.startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例,key对应.bpmn文件Process的id,使用key值启动,默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID:" + p1.getId());// 流程实例ID
		System.out.println("流程定义ID:" + p1.getProcessDefinitionId());// 流程定义ID
		System.out.println("流程实例名称:" + p1.getName());// 流程实例名称

	}

	/**
	 * 查询当前人的个人任务 TaskService <br/>
	 *
	 * act_ru_task
	 */
	@Test
	public void findMyTask() {
		System.out.println("------------------查询当前人的个人任务-----------------------");
		String assignee = "杨";// 经办人 UesrTask-MainCofig,目前该谁操作
		List<Task> list = pe.getTaskService()// 正在执行的任务管理
				.createTaskQuery()// 创建查询任务
				.taskAssignee(assignee)// 指定个人任务查询,指定经办人
				.list();
		if (list != null && list.size() > 0) {
			for (Task t : list) {
				System.out.println("任务ID:" + t.getId());
				System.out.println("任务名称:" + t.getName());
				System.out.println("任务的创建时间:" + t.getCreateTime());
				System.out.println("任务的经办人:" + t.getAssignee());
				System.out.println("流程实例ID:" + t.getProcessDefinitionId());
				System.out.println("执行对象ID:" + t.getExecutionId());
				System.out.println("流程定义ID:" + t.getProcessInstanceId());
				System.out.println("--------------------------------------------");
			}

		} else {
			System.out.println("没有查询到内容! ");
		}

	}

	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyTask() {
		System.out.println("------------------完成我的任务-----------------------");
		String taskId = "17509";// 任务ID,创建任务时生成的act_ru_task表中id
		pe.getTaskService()// 与正在执行的任务管理相关的Service
				.complete(taskId);// 完成任务id
		System.out.println("完成任务,id:" + taskId);
	}

}
