package codegen.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.model.SVG;
import codegen.spark.model.SVGNode;
import codegen.spark.service.JickCodeService;
import codegen.spark.service.StorageService;

@Controller
@RequestMapping("/svg")
public class SvgController {
	private static final Logger LOG = LoggerFactory.getLogger(SvgController.class);

	@Autowired
	StorageService storageService;

	@Autowired
	JickCodeService jickCodeService;

	@RequestMapping(value = { "output/{svg}" })
	public String outputSvg(@PathVariable String svg, Map<String, Object> map) {
		String svgJson = storageService.querySvgJson(svg);
		JSONObject json = JSONObject.parseObject(svgJson);
		JSONArray chart = (JSONArray) json.get("chart");

		List<SVGNode> nodes = chart.toJavaList(SVGNode.class);
		SVG svgobj = new SVG();
		svgobj.setParams(new HashMap<String, String>());
		svgobj.setNodes(nodes);
		String code = "";
		try {
			jickCodeService.toCode(svgobj);
		} catch (Exception e) {
			LOG.error("输出代码异常:{}",e);
			e.printStackTrace();
			code = "output code exception!";
		}
		map.put("svg", svg);
		map.put("code", code);
		return "svg/output";
	}
	
	
	@RequestMapping(value = { "all" })
	public String all(Map<String, Object> map) {
		List<String> jnodeNames=storageService.getSvgKeys();
		map.put("svglist",jnodeNames);
		return "/svg/all";
	}
	
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteJnode(HttpServletRequest request) {
		String node = request.getParameter("node");
		boolean success=storageService.delSvgNode(node);

		JSONObject json = getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}
	
	/**
	 *  edit,new 暂时未使用
	 * 
	 */
	
	@RequestMapping(value = { "new" })
	public String newSvg(Map<String, Object> map) {
		map.put("jnodename","");
		map.put("dialog","");
		map.put("ftl","");
		return "uploadJnode";
	}	
	
	@RequestMapping(value = { "edit/{jnodename}" })
	public String editSvg(@PathVariable String jnodename,Map<String, Object> map) {
		map.put("jnodename",jnodename);
		String json = storageService.getJNode(jnodename);
		
		LOG.info("jnode内容:{}",json);
		if (StringUtils.isNotEmpty(json)) {
			JSONObject jnode=JSONObject.parseObject(json);
			map.put("ftl",jnode.get("ftl"));
			map.put("dialog",jnode.get("dialog"));
		}
		return "uploadJnode";
	}
	
//	/**
//	 * 上传JNode节点配置信息
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "uploadJnode", method = { RequestMethod.POST })
//	@ResponseBody
//	public String uploadJnode(HttpServletRequest request) {
//		String name = request.getParameter("name");
//		String data = request.getParameter("data");
//		String props = request.getParameter("propdialog");
//		JSONObject json=new JSONObject();
//		json.put("ftl", data);
//		json.put("dialog",props);
//		String jnodeData=json.toJSONString();
//		LOG.info("###uploadJnode name={},jnodeData={}", name,jnodeData);
//		storageService.putJNode(name, jnodeData);
//		JSONObject ret = getJsonResp("0", "SUCCESS", null);
//		return ret.toJSONString();
//	}
//	
	
	
	private JSONObject getJsonResp(String code, String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		if (data != null) {
			json.put("data", data);
		}
		return json;
	}
}
