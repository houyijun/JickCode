package codegen.spark.jnode;

import java.util.ArrayList;
import java.util.List;

/**
 * JNode super class
 * xml config jnode tree structure
 * 
 * @author Yijun Hou
 *
 */
public class JNode {
	
	protected JNodeData nodeData;
	public JNodeData getNodeData() {
		return nodeData;
	}


	public void setNodeData(JNodeData nodeData) {
		this.nodeData = nodeData;
	}

	protected List<JNode> children=new ArrayList<JNode>();

	public List<JNode> getChildren() {
		return children;
	}


	public void setChildren(List<JNode> children) {
		this.children = children;
	}
	
	/**
	 * output code for this jnode
	 * child class override this method()
	 * @return
	 */
	public String toCode() throws Exception{
		return "";
	}

}
