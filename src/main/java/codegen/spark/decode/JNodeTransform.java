package codegen.spark.decode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codegen.spark.jnode.JNode;
import codegen.spark.jnode.*;
import codegen.spark.jnode.source.*;
import codegen.spark.jnode.sink.*;

/**
 * 
 * @author Yijun Hou
 *
 */
public class JNodeTransform {
	private static final Logger LOG = LoggerFactory.getLogger(JNodeTransform.class);

	
	public static JNode transform(JNodeData nodeData) {
		if (nodeData.getNodeType().equalsIgnoreCase("jnode")) {
			String type = (String) nodeData.getAttrs().get("type");
			if (type.equalsIgnoreCase("map")) {
				return JNodeMap.transform(nodeData);

			} else if (type.equalsIgnoreCase("sourceFile")) {
				return JNodeSourceFile.transform(nodeData);

			} else if (type.equalsIgnoreCase("sinkFile")) {
				return JNodeSinkFile.transform(nodeData);

			}else {
				JNode node =new JNode();
				node.setNodeData(nodeData);
				return node;
			}
		}
		return null;
	}
	
}
