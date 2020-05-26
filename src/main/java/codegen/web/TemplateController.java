package codegen.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;

@Controller
public class TemplateController {
	@Autowired
	KVDB kvDB;
	
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request) {
		String template = request.getParameter("template");
		boolean success=kvDB.del(template);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}

	@RequestMapping(value = "template/add", method = { RequestMethod.POST })
	@ResponseBody
	public String add(HttpServletRequest request) {
		String node = request.getParameter("name");
		kvDB.update(node,"");
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", "");
		return json.toJSONString();
	}
	
	@RequestMapping(value = "template/rename", method = { RequestMethod.POST })
	@ResponseBody
	public String rename(HttpServletRequest request) {
		String oldName = request.getParameter("oldName");
		String newName = request.getParameter("newName");
		kvDB.rename(oldName,newName);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", "");
		return json.toJSONString();
	}
	
	@RequestMapping(value = { "{template}/info" })
	public String info(@PathVariable String template,Map<String, Object> map) {
		map.put("divname", "/templateinfo.ftl");
		map.put("template", template);
		
		//add jnode all
//		map.put("divname", "/jnode/all.ftl");
		List<String> jnodeNames=kvDB.getTemplateKeys(template,KVDB.JNODE);
		map.put("jnodelist",jnodeNames);
		
		// add model all
//		map.put("divname", "/model/all.ftl");
		List<String> nodes= kvDB.getTemplateKeys(template,KVDB.MODEL);
		map.put("nodes",nodes);
		
		// old code
		map.put("menu","projects");
//		List<String> codetypes=kvDB.getTemplateKeys(template,KVDB.MODEL);
//		map.put("codetypes", codetypes);
		return "/frame";
	}
	
}
