package codegen.spark.model;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SVGNode {
	private String id;
	private String label;
	private String name;
	private String style;
	private JSONObject props;
	private JSONArray linked;
	private JSONArray link;
	
	//id+name可表示唯一的nodeId
	public String getNodeId() {
		return id+name;
	}
	
	public String getLabelName() {
		return name+id;
	}
	
	//父节点，是要要特殊处理的，特别是在存在多个父节点的情况下
	private List<String> fathers;
	
	public List<String> getFathers() {
		return fathers;
	}
	public void setFathers(List<String> fathers) {
		this.fathers = fathers;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public JSONObject getProps() {
		return props;
	}
	public void setProps(JSONObject props) {
		this.props = props;
	}
	public JSONArray getLinked() {
		return linked;
	}
	public void setLinked(JSONArray linked) {
		this.linked = linked;
	}
	public JSONArray getLink() {
		return link;
	}
	public void setLink(JSONArray link) {
		this.link = link;
	}
	
	
	
}
