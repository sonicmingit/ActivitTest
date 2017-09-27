package win.sonic.b_processdefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 流程定义
 */
public class ProcessDefinitionTest {
	/** 创建一个核心引擎 */
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义classpath路径加载文件
	 */
	@Test
	public void deploymentProcessDefinition_classpath() {
		String diagram = "diagrams/ask";// 流程路径
		String bpmn = diagram + ".bpmn";// bpmn文件
		String png = diagram + ".png";// png文件
		System.out.println("----------------------部署流程定义config-------------------");
		Deployment dep = pe.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("流程定义")// 添加部署名称
				.addClasspathResource(bpmn)// 从classpath的资源中加载,一次只能加载一个
				.addClasspathResource(png)// 从classpath的资源中加载,一次只能加载一个
				.deploy();// 完成部署
		System.out.println("部署ID:" + dep.getId());
		System.out.println("部署名称:" + dep.getName());

	}

	/**
	 * 部署流程定义zip
	 */
	@Test
	public void deploymentProcessDefinition_zip() {
		// 加载本地资源文件路径 获取类.加载类.加载类资源文件
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/ask.zip");
		ZipInputStream zip = new ZipInputStream(in);
		System.out.println("----------------------部署流程定义zip-------------------");
		Deployment dep = pe.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("流程定义zip")// 添加部署名称
				.addZipInputStream(zip)// 从zip加载资源文件
				.deploy();// 完成部署
		System.out.println("部署ID:" + dep.getId());
		System.out.println("部署名称:" + dep.getName());

	}

	/**
	 * 查询流程定义
	 */
	@Test
	public void findProcessDefinition() {
		System.out.println("----------------------按条件查询流程定义-------------------");
		List<ProcessDefinition> list // 返回结果集
		// long count //返回结果数量
		// ProcessDefinition pd //返回单条结果
				= pe.getRepositoryService()// 与流程定义和部署对象相关的Service
						.createProcessDefinitionQuery()// 创建一个流程定义查询
						/** 指定条件查询,对应 act_re_procdef表,相当于where条件 */
						// .deploymentId(deploymentId)//使用部署对象ID查询(DEPLOYMENT_ID)
						// .processDefinitionId(processDefinitionId)//使用流程定义的ID查询(ID_)key.version.deployment_id
						// .processDefinitionKey(processDefinitionKey)//使用流程定义的key查询(KEY_)-集合
						// .processDefinitionNameLike(processDefinitionNameLike)//使用流程定义名称模糊查询(NAME_)
						// ...很多封装查询方式!
						/** 排序 */
						.orderByDeploymentId().asc()// 按照部署对象ID升序排列
						// .orderByProcessDefinitionKey().desc()//按照流程定义的key降序排列
						/** 查询返回结果集 */
						.list();// 返回一个集合列表,封装流程定义.对应上面查询条件返回值
		// .singleResult();//返回唯一结果集
		// .count();//返回结果集数量
		// .listPage(firstResult, maxResults);//分页查询(开始页,共页数)

		/** 输出结果集 */
		if (list != null && list.size() > 0) {
			for (ProcessDefinition p : list) {
				System.out.println("流程定义id:" + p.getId());// 流程定义的key+版本+随机生成数
				System.out.println("流程定义名称:" + p.getName());// 对应bpmn文件中的name值
				System.out.println("流程定义key:" + p.getKey());// 对应bpmn文件中的key值
				System.out.println("流程定义版本:" + p.getVersion());// 当前流程定义key相同,自动加一
				System.out.println("资源名称bpmn:" + p.getResourceName());
				System.out.println("资源名称png:" + p.getDiagramResourceName());
				System.out.println("部署对象id :" + p.getDeploymentId());
				System.out.println("--------------------------------------------");
			}
		} else {
			System.out.println("没有查询结果!");
		}
		/** 输出结果数量,对应count */
		// System.out.println("共查询到:" + count + "条结果!");
	}

	/**
	 * 删除流程定义
	 */
	@Test
	public void deletProcessDefinition() {
		// 使用部署id删除
		String deploymentId = "67505";// (唯一的)
		/**
		 * 不带级联删除,只能删除没有启动的流程 如果流程启动就会抛出异常
		 */
		// pe.getRepositoryService()// 获取与流程定义和部署对象相关的Service
		// .deleteDeployment(deploymentId);

		/** 级联删除,不管流程是否启动都能删除 */
		try {
			pe.getRepositoryService()// 获取与流程定义和部署对象相关的Service
					.deleteDeployment(deploymentId, true);
			System.out.println(deploymentId + "删除成功!");
		} catch (Exception e) {
			System.out.println(deploymentId + " 没有这个部署任务!");
		}

	}

	/**
	 * 查看流程图
	 */
	@Test
	public void viewProcessDefinition() {
		/** 将生成的图片放到文件夹下 */
		// 使用部署id删除
		String deploymentId = "57501";
		/** 获取图片资源名称 */
		String resourceName = "";
		List<String> list = pe.getRepositoryService()//
				.getDeploymentResourceNames(deploymentId);
		// 找到PNG图片名
		if (list != null && list.size() > 0) {
			for (String name : list) {
				if (name.indexOf(".png") >= 0) {
					resourceName = name;
				}

			}
		}
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

	/**
	 * 附加功能:查询最新版本的流程定义
	 */
	@Test
	public void findNewProcessDefinition() {
		// 获取按照key值排列的ProcessDefinition集合
		// SELECT * FROM act_re_procdef ORDER BY KEY_ DESC
		List<ProcessDefinition> list = pe.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.orderByProcessDefinitionKey().asc()// 使用流程定义升序排列(key相同放一起,version升序排列)
				.list();
		// 创建一个临时map为了保存最新版本
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
		if (list != null && list.size() > 0) {
			// 循环覆盖旧版本的key(version后面高版本覆盖前面低版本)
			for (ProcessDefinition pd : list) {
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
		// 输出最新版本
		if (list != null && list.size() > 0) {
			for (ProcessDefinition p : pdList) {
				System.out.println("流程定义id:" + p.getId());// 流程定义的key+版本+随机生成数
				System.out.println("流程定义名称:" + p.getName());// 对应bpmn文件中的name值
				System.out.println("流程定义key:" + p.getKey());// 对应bpmn文件中的key值
				System.out.println("流程定义版本:" + p.getVersion());// 当前流程定义key相同,自动加一
				System.out.println("资源名称bpmn:" + p.getResourceName());
				System.out.println("资源名称png:" + p.getDiagramResourceName());
				System.out.println("部署对象id :" + p.getDeploymentId());
				System.out.println("--------------------------------------------");
			}
		} else {
			System.out.println("没有查询结果!");
		}

	}

	/**
	 * 附加功能:删除流程定义(删除key相同的所有不同版本的流程定义)
	 */
	@Test
	public void deleteProcessDefinition() {
		// 流程定义key
		String processDefinitionKey = "ask";
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
}
