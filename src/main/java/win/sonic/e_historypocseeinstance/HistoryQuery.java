package win.sonic.e_historypocseeinstance;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class HistoryQuery {
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();

	/* 查询历史的流程实例 act_hi_procinst */
	@Test
	public void findHistoryProcessInstance() {
		// 流程实例id
		String processInstanceId = "92501";
		// 历史流程实例
		HistoricProcessInstance hpi = pe.getHistoryService()// 与历史数据有关的service
				.createHistoricProcessInstanceQuery()// 历史流程实例查询
				.processInstanceId(processInstanceId)// 根据流程实例id查询
				.orderByProcessInstanceStartTime().asc()// 根据开始时间升序查询
				.singleResult();// 返回单例
		// 输出内容
		System.out.println("历史id:" + hpi.getId() + " 名称:" + hpi.getName() + " 开始时间:" + hpi.getStartTime() + " 结束时间:"
				+ (hpi.getEndTime() == null ? "还没结束!" : hpi.getEndTime()));
	}

	/* 查询历史活动 act_hi_actinst; */
	@Test
	public void findHistoryActiviti() {
		// 流程实例id
		String processInstanceId = "92501";
		List<HistoricActivityInstance> list = pe.getHistoryService()// 与历史有关的服务
				.createHistoricActivityInstanceQuery()// 创建历史活动查询
				.processInstanceId(processInstanceId)// 根据流程实例id查询
				.list();// 返回单例
		// 输出内容
		if (list != null && list.size() > 0) {
			for (HistoricActivityInstance hai : list) {
				System.out.println(hai.getActivityId() + " " + hai.getAssignee() + " " + hai.getActivityName());
				System.out.println("------------------------------------------");
			}
		}

	}

	/* 查询历史任务 act_hi_taskinst */
	@Test
	public void findHistoryTaskInstance() {
		// 流程实例id
		String assignee = "郝";
		List<HistoricTaskInstance> list = pe.getHistoryService()// 创建历史查询
				.createHistoricTaskInstanceQuery()// 历史任务查询
				.taskAssignee(assignee)// 根据经办人查询
				.list();
		// 输出内容
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance hti : list) {
				System.out.println(hti.getAssignee() + " " + hti.getCreateTime() + " " + hti.getName());
				System.out.println("------------------------------------------");
			}
		}
	}

	/* 查询历史流程变量 act_hi_varinst */
	@Test
	public void findHistoryProcessVariable() {
		List<HistoricVariableInstance> list = pe.getHistoryService()//
				.createHistoricVariableInstanceQuery()// 创建一个历史的流程变量实例
				.variableName("请假天数")// 根据变量名称查询
				.list();

		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance his : list) {
				System.out.println("请假天数"+his.getValue());
			}
		}
	}

}
