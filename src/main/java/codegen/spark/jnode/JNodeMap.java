package codegen.spark.jnode;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class JNodeMap extends JNode{
	
	private String expression="";
	

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	
	@Override
	public String toCode() throws Exception{
		JNodeData data=super.nodeData;
		Map<String, String> map=new HashMap<String, String>();
		map.put("rddname", data.getName());
		map.put("fromname",data.getFromName());
		map.put("expression", expression);
		String templateString="val ${rddname} = ${fromname}.map("+
				"${expression})\n";
		StringWriter result = new StringWriter();
		Template t = new Template("name1", new StringReader(templateString), new Configuration());
		t.process(map, result);
		return result.toString();
	}
	
	public static JNodeMap transform(JNodeData ori) {
		JNodeMap newnode = new JNodeMap();
		newnode.setNodeData(ori);
		for (JNodeData child :ori.getChildren()) {
			if (child.getNodeType().equalsIgnoreCase("expression")) {
				newnode.expression=child.getText();
			}
		}
		return newnode;
	}
}
