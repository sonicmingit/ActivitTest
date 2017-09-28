package win.sonic.a_test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 流程模版
 */
public class PRO {
	// 创建一个核心引擎
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
	TaskService task = pe.getTaskService();
	RuntimeService run = pe.getRuntimeService();

	/**
	 * 部署流程定义
	 * 
	 * @param bpmnName
	 *            bpmn文件名
	 * @param name
	 *            部署对象名称
	 * @param obj
	 *            当前类,传入this
	 */
	@Test
	public void deploymentProcessDefinition(String bpmnName, String name, Object obj) {
		InputStream bpmn = obj.getClass().getResourceAsStream(bpmnName + ".bpmn");
		InputStream png = obj.getClass().getResourceAsStream(bpmnName + ".png");

		Deployment dep = pe.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name(name)// 添加部署名称
				.addInputStream(bpmnName + ".bpmn", bpmn).addInputStream(bpmnName + ".png", png).deploy();// 完成部署
		System.out.println("----------------------部署流程定义成功-------------------");
		System.out.println("部署ID:" + dep.getId());
		System.out.println("部署名称:" + dep.getName());
	}

	/**
	 * 启动流程实例
	 * 
	 * @param processDefinitionKey
	 *            启动流程实例ID(自己设置的)
	 */
	@Test
	public void startProcessInstance(String processDefinitionKey) {

		ProcessInstance p1 = pe.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
				.startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例,key对应.bpmn文件Process的id,使用key值启动,默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID:" + p1.getId());// 流程实例ID
		System.out.println("流程定义ID:" + p1.getProcessDefinitionId());// 流程定义ID
		p1.getActivityId();
		System.out.println("---------------------启动流程实例成功!--------------------");

	}

	/**
	 * 查询当前经办人的个人任务
	 * 
	 * @param 经办人
	 *            UesrTask-MainCofig
	 */
	@Test
	public void findMyTask(String assignee) {

		List<Task> list = pe.getTaskService()// 正在执行的任务管理
				.createTaskQuery()// 创建查询任务
				.taskAssignee(assignee)// 指定个人任务查询,指定经办人
				.list();
		System.out.println("------------------当前经办人的个人任务-----------------------");
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
	 * @Description: 完成当前任务
	 * @param taskId
	 *            任务ID,创建任务时生成的act_ru_task表中id
	 * @param values
	 *            流程变量map集合
	 */
	@Test
	public void completeMyTask(String taskId, Map values) {

		pe.getTaskService()// 与正在执行的任务管理相关的Service
				.complete(taskId, values);// 完成任务id

		System.out.println("------------------当前任务已完成任务-----------------------");
		System.out.println("完成任务:id:" + taskId);
	}

	/**
	 * 删除流程定义(删除key相同的所有不同版本的流程定义)
	 * 
	 * @param processDefinitionKey
	 *            要删除的流程定义key
	 */
	@Test
	public void deleteProcessDefinition(String processDefinitionKey) {

		// 先用流程定义的key查询流程定义,查询出所有版本
		// SELECT * FROM act_re_procdef WHERE KEY_ = "test"
		List<ProcessDefinition> list = pe.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey)// 使用流程定义key查询
				.list();

		// 遍历删除
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				// 获取部署ID
				String deploymentId = pd.getDeploymentId();
				// 级联删除
				pe.getRepositoryService().deleteDeployment(deploymentId, true);
				System.out.println("------------已删除:" + pd.getId() + "-------------");
			}
		}
	}

	
	/**
	 *  下载流程图资源到D盘
	 * @param deploymentId 部署ID
	 */
	@Test
	public void downloadProcessDefinition(String deploymentId) {
		/** 获取图片资源名称 */
		String resourceName = "";
		List<String> list = pe.getRepositoryService()//
				.getDeploymentResourceNames(deploymentId);
		// 找到PNG图片名
		if (list != null && list.size() > 0) {
			for (String name : list) {
				resourceName = name;
				// 获取图片的输入流
				InputStream in = pe.getRepositoryService()// 获取与流程定义和部署对象相关的Service
						.getResourceAsStream(deploymentId, resourceName);// 通过部署id和文件名获取输入流
				// 将图片生成到d盘目录下
				File file = new File("e:/" + resourceName);
				// 将输入流的图片写到目录下
				try {
					FileUtils.copyInputStreamToFile(in, file);
					System.out.println("e:/" + resourceName + "已复制完成!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
