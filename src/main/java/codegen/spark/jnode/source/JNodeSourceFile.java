package codegen.spark.jnode.source;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import codegen.spark.jnode.JNode;
import codegen.spark.jnode.JNodeData;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * example:
 * val csv_data = spark.read.csv("file:///D:/java_workspace/fun_test.csv") //本地文件
val csv_data = spark.read.csv("hdfs:///tmp/fun_test.csv") //HDFS文件
val csv_data = spark.read.format("csv").load("hdfs:///tmp/fun_test.csv") //另一种写法

// 除了csv之外, 还有支持很多数据类型
spark.read.jdbc
spark.read.json
spark.read.orc
spark.read.parquet
spark.read.textFile
 * 
 * @author Yijun Hou
 *
 */
public class JNodeSourceFile extends JNode{
	private String path="";
	private String format="cvs";

	@Override
	public String toCode() throws Exception{
		JNodeData data=super.nodeData;
		Map<String, String> map=new HashMap<String, String>();
		map.put("rddname", data.getName());
		map.put("format", format);
		map.put("path", path);
		String templateString="val ${rddname} = spark.read.format(\"${format}\")\n"+
				".load(\"${path}\")\n";
		StringWriter result = new StringWriter();
		Template t = new Template("name1", new StringReader(templateString), new Configuration());
		t.process(map, result);
		return result.toString();
	}
	
	public static JNodeSourceFile transform(JNodeData ori) {
		JNodeSourceFile node = new JNodeSourceFile();
		node.setNodeData(ori);
		for (JNodeData child :ori.getChildren()) {
			if (child.getNodeType().equalsIgnoreCase("path")) {
				node.path=child.getText();
			}
			if (child.getNodeType().equalsIgnoreCase("format")) {
				node.format=child.getText();
			}
		}
		return node;
	}
}
