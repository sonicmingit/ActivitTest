package win.sonic.TRADEPROMO;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import win.sonic.a_test.PRO;

public class TRADEPROMO {

	/** 预置方法 */
	PRO p = new PRO();

	/* 部署流程定义 */
	@Test
	public void deploymentProcessDefinition() {
		p.deploymentProcessDefinition("TRADEPROMO", "贸促会测试",this);
	}

	/* 启动流程实例 */
	@Test
	public void startProcessInstance() {
		p.startProcessInstance("TRADEPROMO");
	}

	/* 查询当前经办人的个人任务 */
	@Test
	public void findMyTaskStart() {
		p.findMyTask("start");
	}
	
	/* 完成当前任务 */
	@Test
	public void completeMyTaskStrat() {	
		Map<String,String> values = new HashMap<String,String>();
		values.put("super", "郝");
		//values.put("argee", "false");
		//values.put("leader", "乔");
		p.completeMyTask("235005",values);

	}
	
	/* 查询当前经办人的个人任务 */
	@Test
	public void findMyTask() {
		p.findMyTask("郝");
	}

	/* 完成当前任务 */
	@Test
	public void completeMyTask() {
		Map<String,String> values = new HashMap<String,String>();
		values.put("super", null);
		values.put("argee", "true");
		p.completeMyTask("237503",values);

	}
}
