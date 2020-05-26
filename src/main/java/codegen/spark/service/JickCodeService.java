package codegen.spark.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;
import codegen.spark.model.SVG;
import codegen.spark.model.SVGNode;
import codegen.spark.model.SVGOpe;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Service
public class JickCodeService {
	private static final Logger LOG = LoggerFactory.getLogger(JickCodeService.class);

	@Autowired
	KVDB kvDB;

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
	public String transfer(String template, String jnodeName, SVGNode node, SVG svg, Map<String, SVGNode> svgMap)
			throws Exception {
		if (jnodeName == null) {
			LOG.error("jnode名称为空");
			return null;
		}
		String jnodeModel = kvDB.getTemplate(template, KVDB.JNODE, jnodeName);

		JSONObject jnodeObj = JSONObject.parseObject(jnodeModel);
		if (!jnodeObj.containsKey("ftl")) {
			LOG.error("jnode代码模板为空");
			return null;
		}
		String ftlText = (String) jnodeObj.get("ftl");

		if (ftlText == null) {
			LOG.error("jnode代码模板为空");
			return null;
		}
		// 获取svg节点配置的属性值
		Map<String, Object> props = json2Map(node.getProps().toJSONString());

		// 给node的父节点排序，1,2,3.。。
		List<String> fathers = getFatherNames(node);
		node.setFathers(fathers);

		Map<String, Object> nodeMap = new HashMap<String, Object>();
		nodeMap.put("nodetype", getNodeName(node));
		nodeMap.put("node", node);
		nodeMap.put("props", props);
		nodeMap.put("params", svg.getParams());
		nodeMap.put("parents", getParents(node, svgMap));
		// 现在可以输出代码了
		String str = str2Str(ftlText, nodeMap);

		return str;
	}

	/**
	 * 父节点列表，处理排序后
	 * 
	 * @param node
	 * @param svgMap
	 * @return
	 * @throws Exception
	 */
	public List<SVGNode> getParents(SVGNode node, Map<String, SVGNode> svgMap) throws Exception {
		List<SVGNode> parents = new ArrayList<SVGNode>();
		if (node.getLinked().size() < 1) {
			return parents;
		}
		for (int i = 0; i < node.getLinked().size(); i++) {
			JSONObject linked = (JSONObject) node.getLinked().get(i);
			String name = linked.getString("name");
			String parent = name.split("\\|")[0];
			if (svgMap.containsKey(parent)) {
				parents.add(svgMap.get(parent));
			}
		}
		return parents;
	}

	public String toCode(String template, SVG svg) throws Exception {
		Map<String, SVGNode> svgMap = getMap(svg);
		String code = "";
		if (svg.getNodes() != null) {
			Queue<SVGNode> queue = SVGOpe.genQueue(svg);
			Iterator<SVGNode> iter = queue.iterator();
			while (iter.hasNext()) {
				SVGNode node = iter.next();
				String subCode = "";
				try {
					subCode = transfer(template, node.getName(), node, svg, svgMap);
				} catch (Exception e) {
					e.printStackTrace();
					subCode = "##Code exception##:" + node.getName() + "," + e.getLocalizedMessage();
				}
				code = code + subCode + "\n";
			}

			// for (int i = 0; i < svg.getNodes().size(); i++) {
			// SVGNode node = svg.getNodes().get(i);
			// String subCode = "";
			// try {
			// subCode = transfer(node.getName(), node, svg);
			// } catch (Exception e) {
			// e.printStackTrace();
			// subCode = "##Code exception##:" + node.getName() +","+
			// e.getLocalizedMessage();
			// }
			// code = code + subCode + "\n";
			// }
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

	private Map<String, SVGNode> getMap(SVG svg) {
		Map<String, SVGNode> map = new HashMap<String, SVGNode>();
		for (SVGNode node : svg.getNodes()) {
			map.put(node.getNodeId(), node);
		}
		return map;
	}
}
