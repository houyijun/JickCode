package codegen.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;
import codegen.spark.service.JickCodeService;

@Controller
public class JnodeController {
	private static final Logger LOG = LoggerFactory.getLogger(JnodeController.class);

	@Autowired
	KVDB kvDB;

	@Autowired
	JickCodeService jickCodeService;

	@RequestMapping(value = { "/{template}/jnode/all" })
	public String all(@PathVariable String template,Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/jnode/all.ftl");
		List<String> jnodeNames=kvDB.getTemplateKeys(template,KVDB.JNODE);
		map.put("jnodelist",jnodeNames);
		return "/frame";
	}
	
	@RequestMapping(value = { "{template}/jnode/new" })
	public String jnode_new(@PathVariable String template,Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/jnode/edit.ftl");
		map.put("jnodename","");
		map.put("dialog","");
		map.put("ftl","");
		return "/frame";
	}
	
	
	@RequestMapping(value = { "{template}/jnode/edit/{jnodename}" })
	public String edit(@PathVariable String template,@PathVariable String jnodename,Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/jnode/edit.ftl");
		map.put("jnodename",jnodename);
		String json =kvDB.getTemplate(template,KVDB.JNODE, jnodename);
		
		LOG.info("jnode内容:{}",json);
		if (StringUtils.isNotEmpty(json)) {
			JSONObject jnode=JSONObject.parseObject(json);
			map.put("ftl",jnode.get("ftl"));
			map.put("dialog",jnode.get("dialog"));
		}
		return "/frame";
	}
	
	/**
	 * 上传JNode节点配置信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "{template}/jnode/edit.do", method = { RequestMethod.POST })
	public String edit_do(@PathVariable String template,HttpServletRequest request) {
		String name = request.getParameter("name");
		String data = request.getParameter("data");
		String props = request.getParameter("propdialog");
		JSONObject json=new JSONObject();
		json.put("ftl", data);
		json.put("dialog",props);
		String jnodeData=json.toJSONString();
		LOG.info("###uploadJnode name={},jnodeData={}", name,jnodeData);
		kvDB.updateTemplate(template,KVDB.JNODE, name,jnodeData);
		return "forward:/"+template+"/jnode/all";
	}
	
	@RequestMapping(value = "{template}/jnode/delete", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteJnode(@PathVariable String template,HttpServletRequest request) {
		String node = request.getParameter("node");
		LOG.info("delete node:{},{}",template,node);
		boolean success=kvDB.delTemplate(template,KVDB.JNODE,node);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}
	
	/**
	 * 导出所有jnode到单个文件
	 */
	@RequestMapping("{template}/jnode/download")
	public void download(@PathVariable String template,HttpServletResponse response) {
		String outFile="nodes.json";
		Map<String,String> nodes =kvDB.getTemplateAll(template,KVDB.JNODE);
		if (nodes==null) {
			Funcs.exportCodeFile(response,outFile,"" );
			return;
		}
		String code = JSONObject.toJSONString(nodes);		
		Funcs.exportCodeFile(response,outFile,code );
	}
	
	@RequestMapping(value = "{template}/jnode/clone", method = { RequestMethod.POST })
	@ResponseBody
	public String clone(@PathVariable String template,HttpServletRequest request) {
		String oldName = request.getParameter("oldName");
		String newName = request.getParameter("newName");
		String value=kvDB.getTemplate(template, KVDB.JNODE, oldName);
		if (value==null) {
			value="";
		}
		kvDB.updateTemplate(template,  KVDB.JNODE, newName, value);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", "");
		return json.toJSONString();
	}
	
	/**
	 * 导出单个Jnode
	 * @param response
	 */
	@RequestMapping("{template}/jnode/downsingle")
	public void downsingle(@PathVariable String template,HttpServletRequest request,HttpServletResponse response) {
		String node = request.getParameter("node");		
		String outFile=node+".txt";
		
		String txt =kvDB.getTemplate(template,KVDB.JNODE,node);
		if (txt==null) {
			txt="";
		}
		JSONObject json =new JSONObject();
		json.put(node, txt);
		String code = JSONObject.toJSONString(json);		
		Funcs.exportCodeFile(response,outFile,code );
	}
	
	
	/**
	 * 批量导入所有jnodes
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("{template}/jnode/import.do")
	public String import_do(@PathVariable String template,@RequestParam(value = "filename") MultipartFile file, HttpServletRequest request) {
		try {
			String content = new String(file.getBytes());
			LOG.info("上传名称={},文件内容={}",file.getName(),content);
			Map<String,String> map =JSONObject.parseObject(content).toJavaObject(Map.class);
			if (map!=null) {
				for (String key:map.keySet()) {
					kvDB.updateTemplate(template,KVDB.JNODE,key,map.get(key));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/"+template+"/jnode/all";
	}
	
}
