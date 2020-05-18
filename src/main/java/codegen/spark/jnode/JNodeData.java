package codegen.spark.jnode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml config jnode tree structure
 * 
 * @author Yijun Hou
 *
 */
public class JNodeData {
	// this node's tag
	protected String nodeType;
	
	// whether is root jnode? root jnode is exactly once
	protected boolean isRoot=false;
	
	// this node's id
	protected String id;
	//parent jnode's id
	protected String fromId;
	
	protected String fromName;
	
	

	public String getFromId() {
		return fromId;
	}


	public void setFromId(String fromId) {
		this.fromId = fromId;
	}


	public String getFromName() {
		return fromName;
	}


	public void setFromName(String fromName) {
		this.fromName = fromName;
	}


	//jnode type
	protected String type;	

	// this node's name
	protected String name;
	// this node's inner text
	protected String text;
	
	// this node's attributes
	protected Map<String,String> attrs=new HashMap<String,String>();

	// this node's child jnodes
	protected List<JNodeData> children=new ArrayList<JNodeData>();
		
	
	public String getNodeType() {
		return nodeType;
	}


	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public boolean isRoot() {
		return isRoot;
	}


	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<JNodeData> getChildren() {
		return children;
	}


	public void setChildren(List<JNodeData> children) {
		this.children = children;
	}


	public void addChild(JNodeData child) {
		children.add(child);
	}
	
	
	public Map<String, String> getAttrs() {
		return attrs;
	}


	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
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
