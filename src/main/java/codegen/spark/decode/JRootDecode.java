package codegen.spark.decode;

import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codegen.spark.jnode.JNode;
import codegen.spark.jnode.JNodeData;
import codegen.spark.jnode.JRoot;

public class JRootDecode {
	private static final Logger LOG = LoggerFactory.getLogger(JRootDecode.class);

	public static JRoot decodeFromXmlElement(Element rootElement) {
		try {
			if (!rootElement.getName().equalsIgnoreCase("spark")) {
				LOG.error("invalid JNode tree format:{}", rootElement.getName());
				return null;
			}
			
			JNodeData rootNodeData=xmlToNodeData(rootElement);
			
			// decode root jnode
			JRoot rootNode = decodeRoot(rootNodeData);
			
			return rootNode;
		} catch (Exception e) {
			LOG.error("decodeFromXmlElement:{}", e);
			return null;
		}
	}
	
	public static JNodeData xmlToNodeData(Element ele) {
		JNodeData data = new JNodeData();
		// get name and props
		data.setNodeType(ele.getName());
		data.setText(ele.getText());
		if (ele.getName().equalsIgnoreCase("spark")) {
			data.setRoot(true);
		}else {
			data.setRoot(false);
		}
		List<Attribute> attributes = ele.getAttributes();
		for (Attribute attr : attributes) {
			data.getAttrs().put(attr.getName(), attr.getValue());
			if (attr.getName().equalsIgnoreCase("name")) {
				data.setName(attr.getValue());
			}
			if (attr.getName().equalsIgnoreCase("id")) {
				data.setId(attr.getValue());
			}
			if (attr.getName().equalsIgnoreCase("fromid")) {
				data.setFromId(attr.getValue());
			}
			if (attr.getName().equalsIgnoreCase("type")) {
				data.setType(attr.getValue());
			}
		}
		List<Element> children = ele.getChildren();
		for (Element child : children) {
			JNodeData childData = xmlToNodeData(child);
			if (childData != null) {
				data.addChild(childData);
			}
		}
		
		linkParents(data);
		return data;
	}
	
	private static void linkParents(JNodeData root) {
		if (!root.isRoot()) {
			return;
		}
		HashMap<String,JNodeData> map =new HashMap<String,JNodeData>();
		for (JNodeData child:root.getChildren()) {
			map.put(child.getId(),child);
		}
		for (JNodeData child:root.getChildren()) {
			String fromId=child.getFromId();
			if (fromId!=null) {
				JNodeData fromNode = map.get(fromId);
				if (fromNode!=null) {
					child.setFromName(fromNode.getName());
				}
			}
		}
		
	}

	
	public static JRoot decodeRoot(JNodeData nodeData) {
		if (!nodeData.isRoot()) {
			LOG.error("decodeRoot:{}",nodeData);
			return null;
		}
		JRoot root = new JRoot();
		for (JNodeData childData:nodeData.getChildren()) {
			JNode node=JNodeTransform.transform(childData);
			if (node!=null) {
				try {
					LOG.info("##jnode:{}",node.toCode());
				}catch(Exception e) {
					LOG.error("jnode:{}",e);
				}
				root.getChildren().add(node);
			}
		}
		
		return root;
	}
	
	/**
	 * output code for jnode tree
	 * @param root
	 * @return
	 * @throws Exception
	 */
	public static String toCode(JRoot root) throws Exception {
		String code = "";
		for (JNode child : root.getChildren()) {
			code += child.toCode() + "\n";
		}
		return code;
	}
}
