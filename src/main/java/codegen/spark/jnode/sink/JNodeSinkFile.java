package codegen.spark.jnode.sink;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import codegen.spark.jnode.JNode;
import codegen.spark.jnode.JNodeData;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * example：
 * 
 * csv_data.coalesce(1)  //设置为一个partition, 这样可以把输出文件合并成一个文件
        .write.mode(SaveMode.Overwrite) 
        .format("com.databricks.spark.csv")
        .save("file:///D:/java_workspace/fun_test.csv")
 * 
 * @author Yijun Hou
 *
 */
public class JNodeSinkFile extends JNode{
	
	protected String path="";
	protected String format="com.databricks.sparl.cvs";

	
	@Override
	public String toCode() throws Exception{
		JNodeData data=super.nodeData;
		Map<String, String> map=new HashMap<String, String>();
		map.put("rddname", data.getName());
		map.put("fromname", data.getFromName());
		map.put("format", format);
		map.put("path", path);
		String templateString="//write to sink\n"+
				"val ${rddname} = ${fromname}.coalesce(1)\n"+
				".write.mode(SaveMode.Overwrite)\n"+
				".format(\"${format}\")\n"+
				".save(\"${path}\")\n";
		StringWriter result = new StringWriter();
		Template t = new Template("name1", new StringReader(templateString), new Configuration());
		t.process(map, result);
		return result.toString();
	}
	
	public static JNodeSinkFile transform(JNodeData ori) {
		JNodeSinkFile node = new JNodeSinkFile();
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
