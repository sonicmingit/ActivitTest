package win.sonic.a_test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Template {

	/** 预置方法 */
	PRO p = new PRO();

	/* 部署流程定义 */
	public void deploymentProcessDefinition() {
		p.deploymentProcessDefinition("bpmnName", "name","object");
	}

	/* 启动流程实例 */
	@Test
	public void startProcessInstance() {
		p.startProcessInstance("processDefinitionKey");
	}

	/* 查询当前经办人的个人任务 */
	@Test
	public void findMyTask() {
		p.findMyTask("assignee");
	}

	/* 完成当前任务 */
	@Test
	public void completeMyTask() {
		Map<String,String> values = new HashMap<String,String>();
		p.completeMyTask("taskId", values);

	}
}
