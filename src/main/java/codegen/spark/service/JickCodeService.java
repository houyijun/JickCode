package codegen.spark.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.model.SVG;
import codegen.spark.model.SVGNode;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Service
public class JickCodeService {
	private static final Logger LOG = LoggerFactory.getLogger(JickCodeService.class);


	@Autowired
	StorageService storageService;

	/**
	 * 从字符串读取template，输出转换后的结果字符串
	 * 
	 * @param beanMap
	 * @param templateStr
	 * @return
	 * @throws Exception
	 */
	private String str2Str(String templateStr, Map<String, Object> beanMap) throws Exception {
		Configuration config = new Configuration();
		config.setObjectWrapper(new DefaultObjectWrapper());
		Template template = new Template("template", new StringReader(templateStr), config);
		StringWriter stringWriter = new StringWriter();
		template.process(beanMap, stringWriter);
		stringWriter.flush();
		stringWriter.close();
		return stringWriter.toString();
	}

	public String file2Str(String templateFile, Map<String, Object> beanMap) throws Exception {
		Configuration config = new Configuration();
		config.setObjectWrapper(new DefaultObjectWrapper());
		Template template = config.getTemplate(templateFile, "UTF-8");
		StringWriter stringWriter = new StringWriter();
		template.process(beanMap, stringWriter);
		stringWriter.flush();
		stringWriter.close();
		return stringWriter.toString();
	}

	/*
	 * json to map<string,object>
	 */
	private Map<String, Object> json2Map(String jsondata) {
		// json字符串
		// jsondata="{\"contend\":[{\"bid\":\"22\",\"carid\":\"0\"},{\"bid\":\"22\",\"carid\":\"0\"}],\"result\":100,\"total\":2}";
		JSONObject obj = JSON.parseObject(jsondata);
		Map<String, Object> data = new HashMap<String, Object>();
		Iterator it = obj.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			data.put(entry.getKey(), entry.getValue());
		}
		return data;
	}

	/**
	 * 转换单个jnode，输出代码
	 * 
	 * 
	 * @param jnodeName
	 * @param node
	 *            单个svg节点信息
	 * @param svg
	 *            整个svg图的信息，防止有用到上下文的地方。
	 * @return
	 */
	public String transfer(String jnodeName, SVGNode node, SVG svg) throws Exception {
		if (jnodeName==null) {
			LOG.error("jnode名称为空");
			return null;
		}
		String jnodeModel = storageService.getJNode(jnodeName);

		 JSONObject jnodeObj = JSONObject.parseObject(jnodeModel);
		String ftlText = (String)jnodeObj.get("ftl");

		if (ftlText==null) {
			LOG.error("jnode代码模板为空");
			return null;
		}
		// 获取svg节点配置的属性值
		Map<String, Object> props = json2Map(node.getProps().toJSONString());

		// 给node的父节点排序，1,2,3.。。
		List<String> fathers = getFatherNames(node);
		node.setFathers(fathers);

		Map<String, Object> nodeMap = new HashMap<String, Object>();
		nodeMap.put("nodetype",getNodeName(node));
		nodeMap.put("node", node);
		nodeMap.put("props",props);
		nodeMap.put("params", svg.getParams());
		// 现在可以输出代码了
		String str = str2Str(ftlText, nodeMap);

		return str;
	}

	public String toCode(SVG svg) throws Exception {
		String code = "";
		if (svg.getNodes() != null) {
			for (int i = 0; i < svg.getNodes().size(); i++) {
				SVGNode node = svg.getNodes().get(i);
				String subCode = "";
				try {
					subCode = transfer(node.getName(), node, svg);
				} catch (Exception e) {
					e.printStackTrace();
					subCode = "##Code exception##:" + node.getName() +","+ e.getLocalizedMessage();
				}
				code = code + subCode + "\n";
			}
		}
		return code;
	}

	private List<String> getFatherNames(SVGNode node) {
		List<String> names = new ArrayList<String>();
		if (node.getLinked() != null && node.getLinked().size() > 0) {
			for (int i = 0; i < node.getLinked().size(); i++) {
				JSONObject father = (JSONObject) node.getLinked().get(i);
				names.add(father.getString("name"));
			}
		}
		return names;
	}

	// 将来要重写的，获取jnode的名称
	private String getNodeName(SVGNode node) {
		return node.getName();
	}
}