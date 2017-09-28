package win.sonic.h_parallelgateway;

import org.junit.Test;

import win.sonic.a_test.PRO;

public class ParallelGateWay {

	/** 预置方法 */
	PRO p = new PRO();

	/* 部署流程定义+启动 */
	@Test
	public void ProcessDefinition() {
		p.deploymentProcessDefinition("parallelgateway", "并行网关", this);
		p.startProcessInstance("parallelgateway");
	}

	/* 查询当前经办人的个人任务 */
	@Test
	public void findMyTask() {
		p.findMyTask("商家");
	}

	/* 完成当前任务 */
	@Test
	public void completeMyTask() {
		p.completeMyTask("172502", null);
		/*
		 * p.completeMyTask("150004", null); p.completeMyTask("155004", null);
		 * p.completeMyTask("162504", null);
		 */

	}
}
