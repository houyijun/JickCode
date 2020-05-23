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
@RequestMapping("/jnode")
public class JnodeController {
	private static final Logger LOG = LoggerFactory.getLogger(JnodeController.class);

	@Autowired
	KVDB kvDB;

	@Autowired
	JickCodeService jickCodeService;

	@RequestMapping(value = { "all" })
	public String all(Map<String, Object> map) {
		map.put("divname", "/jnode/all.ftl");
		List<String> jnodeNames=kvDB.getKeys(KVDB.JNODE);
		map.put("jnodelist",jnodeNames);
		return "/frame";
	}
	
	@RequestMapping(value = { "new" })
	public String jnode_new(Map<String, Object> map) {
		map.put("divname", "/jnode/addjnode.ftl");
		map.put("jnodename","");
		map.put("dialog","");
		map.put("ftl","");
		return "/frame";
	}
	
	
	@RequestMapping(value = { "edit/{jnodename}" })
	public String edit(@PathVariable String jnodename,Map<String, Object> map) {
		map.put("divname", "/jnode/edit.ftl");
		map.put("jnodename",jnodename);
		String json =kvDB.get(KVDB.JNODE, jnodename);
		
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
	@RequestMapping(value = "edit.do", method = { RequestMethod.POST })
	public String edit_do(HttpServletRequest request) {
		String name = request.getParameter("name");
		String data = request.getParameter("data");
		String props = request.getParameter("propdialog");
		JSONObject json=new JSONObject();
		json.put("ftl", data);
		json.put("dialog",props);
		String jnodeData=json.toJSONString();
		LOG.info("###uploadJnode name={},jnodeData={}", name,jnodeData);
		kvDB.saveOrUpdate(KVDB.JNODE, name,jnodeData);
		return "forward:/jnode/all";
	}
	
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteJnode(HttpServletRequest request) {
		String node = request.getParameter("node");
		boolean success=kvDB.del(KVDB.JNODE,node);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}
	
	/**
	 * 导出所有jnode到单个文件
	 */
	@RequestMapping("download")
	public void download(HttpServletResponse response) {
		String outFile="nodes.json";
		Map<String,String> nodes =kvDB.getAll(KVDB.JNODE);
		if (nodes==null) {
			Funcs.exportCodeFile(response,outFile,"" );
			return;
		}
		String code = JSONObject.toJSONString(nodes);		
		Funcs.exportCodeFile(response,outFile,code );
	}
	
	/**
	 * 导出单个Jnode
	 * @param response
	 */
	@RequestMapping("downsingle")
	public void downsingle(HttpServletRequest request,HttpServletResponse response) {
		String node = request.getParameter("node");		
		String outFile=node+".txt";
		
		String txt =kvDB.get(KVDB.JNODE,node);
		if (txt==null) {
			txt="";
		}
		JSONObject json =new JSONObject();
		json.put(node, txt);
		String code = JSONObject.toJSONString(json);		
		Funcs.exportCodeFile(response,outFile,code );
	}
	
	
	
	@RequestMapping(value = { "import" })
	public String importpage(Map<String, Object> map) {
		map.put("divname", "/jnode/import.ftl");
		return "/frame";
	}
	/**
	 * 批量导入所有jnodes
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("import.do")
	public String import_do(@RequestParam(value = "filename") MultipartFile file, HttpServletRequest request) {
		try {
			String content = new String(file.getBytes());
			LOG.info("上传名称={},文件内容={}",file.getName(),content);
			Map<String,String> map =JSONObject.parseObject(content).toJavaObject(Map.class);
			if (map!=null) {
				for (String key:map.keySet()) {
					kvDB.saveOrUpdate(KVDB.JNODE,key,map.get(key));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/jnode/all";
	}
	
}
