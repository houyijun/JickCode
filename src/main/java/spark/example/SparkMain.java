package spark.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codegen.spark.decode.JRootDecode;
import codegen.spark.jnode.JRoot;
import codegen.spark.utils.FreeMakerUtil;

public class SparkMain {
	private static final Logger LOG = LoggerFactory.getLogger(SparkMain.class);

	static String configfile = "src/main/resources/model/spark.xml";
	static String templatefile = "src/main/resources/model/spark.ftl";
	static String outfile = "src/main/resources/class/Example.scala";

	public static void main(String[] args) throws Exception {
		JRoot sparkRoot = getSparkRootNode(configfile);
		
		String toCode = JRootDecode.toCode(sparkRoot);
		LOG.info("jnode code:{}",toCode);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("code_generated",toCode);
		FreeMakerUtil.Ftl2File(map, templatefile, outfile);
	}

	
	/**
	 * 解析spark的配置bean文件，生成JickNode对象树
	 * 
	 * @param configfile
	 * @return
	 */
	private static JRoot getSparkRootNode(String configfile) {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			InputStream is = new FileInputStream(new File(configfile));
			Document document = saxBuilder.build(is);
			Element rootElement = document.getRootElement();
			JRoot node = JRootDecode.decodeFromXmlElement(rootElement);
			return node;
		} catch (Exception e) {
			LOG.error("getSparkRootNode:{}", e);
			return null;
		}
	}

	private static Map<String, Object> getTestBean() {
		Map<String, Object> beanMap = new HashMap<String, Object>();
		beanMap.put("beanName", "User");
		beanMap.put("interfaceName", "User");
		List<Map<String, String>> paramsList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 4; i++) {
			Map<String, String> tmpParamMap = new HashMap<String, String>();
			tmpParamMap.put("fieldNote", "fieldNote" + i);
			tmpParamMap.put("fieldType", "String");
			tmpParamMap.put("fieldName", "fieldName" + i);
			paramsList.add(tmpParamMap);
		}
		beanMap.put("params", paramsList);
		return beanMap;
	}
}
