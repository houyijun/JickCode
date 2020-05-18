package codegen.spark.model;

import java.util.List;
import java.util.Map;


public class SVG {
	private Map<String,String> params;
	private List<SVGNode>  nodes;
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public List<SVGNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<SVGNode> nodes) {
		this.nodes = nodes;
	}
	
}
