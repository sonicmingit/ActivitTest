package win.sonic.c_processinstance;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 流程实例
 */
public class ProcessInstanceTest {
	/** 创建一个核心引擎 */
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();

	/** 1.部署流程定义 act_re_deployment act_re_procdef act_ru_execution */
	@Test
	public void deploymentProcessDefinition_classpath() {
		String diagram = "diagrams/ask";// 流程路径
		String bpmn = diagram + ".bpmn";// bpmn文件
		String png = diagram + ".png";// png文件
		System.out.println("----------------------1.部署流程定义config-------------------");
		Deployment dep = pe.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("请假流程")// 添加部署名称
				.addClasspathResource(bpmn)// 从classpath的资源中加载,一次只能加载一个
				.addClasspathResource(png)// 从classpath的资源中加载,一次只能加载一个
				.deploy();// 完成部署
		System.out.println("部署ID:" + dep.getId());
		System.out.println("部署名称:" + dep.getName());

	}

	/** 2.启动流程实例 */
	@Test
	public void startProcessInstance() {
		System.out.println("---------------------2.启动流程实例--------------------");
		String processDefinitionKey = "ask";// 流程id Process-id
		ProcessInstance p1 = pe.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
				.startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例,key对应.bpmn文件Process的id,使用key值启动,默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID:" + p1.getId());// 流程实例ID
		System.out.println("流程定义ID:" + p1.getProcessDefinitionId());// 流程定义ID
		System.out.println("流程实例名称:" + p1.getName());// 流程实例名称

	}

	/**
	 * 3.查询当前个人任务 act_ru_task
	 */
	@Test
	public void findMyTask() {
		System.out.println("------------------查询当前人的个人任务 act_ru_task-----------------------");
		String assignee = "杨";// 经办人 UesrTask-MainCofig,目前该谁操作
		List<Task> list = pe.getTaskService()// 正在执行的任务管理
				.createTaskQuery()// 创建查询任务 act_ru_task
				/** 查询条件(where) */
				// .taskAssignee(assignee)// 指定个人任务查询,指定经办人查询
				// .taskCandidateUser(candidateUser)//组任务的经办人查询
				// .processDefinitionId(processDefinitionId)//流程定义id查询
				// .executionId(executionId);//执行对象id查询TaskQuery
				/** 排序 */
				.orderByTaskId().asc()// 按照任务ID升序排列
				// .orderByProcessDefinitionKey().desc()//按照流程定义的key降序排列
				/** 返回结果集 */
				.list();
		// .singleResult();//返回唯一结果集
		// .count();//返回结果集数量
		// .listPage(firstResult, maxResults);//分页查询(开始页,共页数)
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
	 * 4.完成流程任务 act_ru_task
	 */
	@Test
	public void completeMyTask() {
		System.out.println("------------------完成我的任务-----------------------");
		String taskId = "60005";// 任务ID,创建任务时生成的act_ru_task表中id
		pe.getTaskService()// 与正在执行的任务管理相关的Service
				.complete(taskId);// 完成任务id
		System.out.println("完成任务,id:" + taskId);
	}

	/** 查询流程状态(判断流程是否结束) */
	@Test
	public void isProcessEnd() {
		String processInstanceId = "60001";
		ProcessInstance pi = pe.getRuntimeService()// 获取正在执行的流程实例和执行对象
				.createProcessInstanceQuery()// 流程实例查询
				.processInstanceId(processInstanceId)// 使用流程实例id查询
				.singleResult();// 返回一个单例,只有一个结果
		/*如果这个流程不存在则说明已经完成,act_ru_execution表中已删除.*/
		if (pi != null) {
			System.out.println(processInstanceId + " 流程正在执行!");
		} else {
			System.out.println(processInstanceId + "已经结束!");
		}

		// 也可以查询act_hi_procinst表中的end_time像是否为空,下面例子
	
	}
	
	
	/**
	 * 查询历史任务
	 */
	@Test
	public void findHistoryTask() {
		System.out.println("---------------历史任务查询 act_hi_taskinst");
		String assignee = "陆";
		List<HistoricTaskInstance> list = pe.getHistoryService()//与历史数据相关的service
				.createHistoricTaskInstanceQuery()//创建历史任务实例查询 act_hi_taskinst
				.taskAssignee(assignee)//指定历史任务的办理人
				.list();//返回集合
		//循环输出历史任务信息
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance hi : list) {
				System.out.println(hi.getId()+" "+hi.getName()+" "+hi.getCreateTime());
			}
		}
	}
	
	@Test
	public void findHistoryProcessInstance() {
		System.out.println("---------------历史流程实例查询 act_hi_procinst--------------");
		List<HistoricProcessInstance> list = pe.getHistoryService()//与历史数据相关的service
				.createHistoricProcessInstanceQuery()//创建历史流程实例查询 act_hi_procinst
				/**查询条件*/
				.list();
		//循环输出历史流程实例信息
		if (list != null && list.size() > 0) {
			for (HistoricProcessInstance his : list) {
				System.out.println(his.getId()+" "+his.getName()+" "+his.getEndTime());
				System.out.println("------------------------------");
			}
		}
	}

}
