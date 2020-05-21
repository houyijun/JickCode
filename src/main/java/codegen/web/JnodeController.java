package codegen.web;

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

import com.alibaba.fastjson.JSONObject;

import codegen.spark.service.JickCodeService;
import codegen.spark.service.StorageService;

@Controller
@RequestMapping("/jnode")
public class JnodeController {
	private static final Logger LOG = LoggerFactory.getLogger(JnodeController.class);

	@Autowired
	StorageService storageService;

	@Autowired
	JickCodeService jickCodeService;

	@RequestMapping(value = { "all" })
	public String jnodes(Map<String, Object> map) {
		List<String> jnodeNames=storageService.getJNodeKeys();
		map.put("jnodelist",jnodeNames);
		return "/jnode/jnodes";
	}
	
	@RequestMapping(value = { "new" })
	public String jnode_new(Map<String, Object> map) {
		map.put("jnodename","");
		map.put("dialog","");
		map.put("ftl","");
		return "uploadJnode";
	}
	
//	@RequestMapping(value = { "uploadform" })
//	public String jnode_uploadForm(Map<String, Object> map) {
//		map.put("jnodename","map");
//		return "uploadJnode";
//	}
	
	@RequestMapping(value = { "uploadform/{jnodename}" })
	public String jnode_uploadForm_path(@PathVariable String jnodename,Map<String, Object> map) {
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
	
	/**
	 * 上传JNode节点配置信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "uploadJnode", method = { RequestMethod.POST })
	@ResponseBody
	public String uploadJnode(HttpServletRequest request) {
		String name = request.getParameter("name");
		String data = request.getParameter("data");
		String props = request.getParameter("propdialog");
		JSONObject json=new JSONObject();
		json.put("ftl", data);
		json.put("dialog",props);
		String jnodeData=json.toJSONString();
		LOG.info("###uploadJnode name={},jnodeData={}", name,jnodeData);
		storageService.putJNode(name, jnodeData);
		JSONObject ret = Funcs.getJsonResp("0", "SUCCESS", null);
		return ret.toJSONString();
	}
	
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteJnode(HttpServletRequest request) {
		String node = request.getParameter("node");
		boolean success=storageService.delJNode(node);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}
	
	
}
