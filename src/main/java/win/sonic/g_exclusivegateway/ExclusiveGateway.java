package win.sonic.g_exclusivegateway;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import win.sonic.a_test.PRO;

public class ExclusiveGateway {
	/** 预置方法 */
	PRO p = new PRO();

	/* 部署流程定义 */
	@Test
	public void deploymentProcessDefinition() {
		p.deploymentProcessDefinition("exclusivegateway", "排他网关",this);
	}

	/* 启动流程实例 */
	@Test
	public void startProcessInstance() {
		p.startProcessInstance("exclusivegateway");
	}

	/* 查询当前经办人的个人任务 */
	@Test
	public void findMyTask() {
		p.findMyTask("财务");
	}

	/* 完成当前任务 */
	@Test
	public void completeMyTask() {
		//设置变量
		Map<String,Object> values = new HashMap<String, Object>();
		values.put("money", 800);
		p.completeMyTask("160005", values);

	}
	
	/* 删除任务 */
	@Test
	public void delete() {
		p.deleteProcessDefinition("exclusivegateway");

	}
	/*获取流程图*/
	@Test
	public void download() {
		p.downloadProcessDefinition("115001");
	}
	
	
}
