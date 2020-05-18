package codegen.spark.jnode;

import java.util.ArrayList;
import java.util.List;

public class JRoot {
	protected List<JNode> children=new ArrayList<JNode>();

	public List<JNode> getChildren() {
		return children;
	}


	public void setChildren(List<JNode> children) {
		this.children = children;
	}
	

}
