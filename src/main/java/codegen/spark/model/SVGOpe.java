package codegen.spark.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.service.JickCodeService;

public class SVGOpe {
	private static final Logger LOG = LoggerFactory.getLogger(SVGOpe.class);


	/**
	 * 按优先级排序，入队列。
	 * 最后生成代码的时候按照Queue的顺序进行。
	 * @param svg
	 * @return
	 */
	public static Queue<SVGNode> genQueue(SVG svg) throws Exception{
		Queue<SVGNode> queue =new LinkedList<SVGNode>();
		if (svg.getNodes()==null || svg.getNodes().isEmpty()) {
			return queue;
		}
		List<SVGNode> nodes=new ArrayList<SVGNode>(svg.getNodes());
		Map<String,SVGNode> map=new HashMap<String,SVGNode>();
		List<String> nodeIds=new ArrayList<String>();
		for (SVGNode node:nodes) {
			nodeIds.add(node.getNodeId());
		}
		
		//如果node没有父节点，则入队列
		//如果有父节点，则看所有父节点有没有处理成功？ 成功的话则可入队列。如果父节点没成功，则将节点重新放回list，等下次处理
		while (nodes.size()>0) {
			SVGNode current=nodes.remove(0);
			
			if (current.getLinked().size()<1) {
				queue.add(current);
				map.put(current.getNodeId(),current);
			}else {
				boolean handled=true;
				for(int i=0;i<current.getLinked().size();i++) {
					JSONObject linked=(JSONObject)current.getLinked().get(i);
					String name=linked.getString("name");
					String parent=name.split("\\|")[0];
					if (!map.containsKey(parent)) {
						if (!nodeIds.contains(parent)) {
							LOG.error("非法的名称:{},{}",name,parent);
							throw new Exception("名称异常svg的名称");
						}
						handled=false;
						break;
					}					
				}
				if (handled) {
					queue.add(current);
					map.put(current.getNodeId(),current);
				}else {
					//不满足条件，再放回去
					
					nodes.add(current);
				}
			}
		}
		return queue;
	}
}
