package codegen.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * 导入导出，显示模板文本：比例spark.jick
 * 
 * @author houyiju
 *
 */
@Controller
public class ModelController {
	private static final Logger LOG = LoggerFactory.getLogger(ModelController.class);

	@Autowired
	KVDB kvDB;
	

	// 导入
	@RequestMapping("{template}/model/import.do")
	public String importModel_do(@PathVariable String template,@RequestParam(value = "filename") MultipartFile file, HttpServletRequest request) {
		try {
			String modelname = request.getParameter("name");
			String modelfile = new String(file.getBytes());
			LOG.info("上传名称={},文件内容={}",modelname,modelfile);
			kvDB.updateTemplate(template,KVDB.MODEL,modelname,modelfile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/"+template+"/model/all";
	}

	/**
	 * 下载模板文件内容
	 * @param name
	 * @param response
	 */
	@RequestMapping("{template}/model/download")
	public void download(@PathVariable String template,String name,HttpServletResponse response) {
		String outFile=name+".txt";
		String modelcontent=kvDB.getTemplate(template,KVDB.MODEL,name);
		if (modelcontent==null) {
			modelcontent="null";
		}
		Funcs.exportCodeFile(response,outFile,modelcontent );
	}
	
	/**
	 * 显示模板内容
	 * 
	 * @param modelname
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "{template}/model/edit/{modelname}" })
	public String edit(@PathVariable String template,@PathVariable String modelname,Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/model/edit.ftl");
		map.put("modelname", modelname);
		map.put("modelcontent",kvDB.getTemplate(template,KVDB.MODEL,modelname));
		return "/frame";
	}
	
	@RequestMapping(value = { "{template}/model/detail/{modelname}" })
	public String detail(@PathVariable String template,@PathVariable String modelname,Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/model/detail.ftl");
		map.put("modelname", modelname);
		map.put("modelcontent",kvDB.getTemplate(template,KVDB.MODEL,modelname));
		return "/frame";
	}
	
	
	@RequestMapping(value = "{template}/model/delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(@PathVariable String template,HttpServletRequest request) {
		String node = request.getParameter("name");
		boolean success=kvDB.delTemplate(template,KVDB.MODEL,node);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}
	
	@RequestMapping(value = { "{template}/model/all" })
	public String all(@PathVariable String template,Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/model/all.ftl");
		List<String> nodes= kvDB.getTemplateKeys(template,KVDB.MODEL);
		map.put("nodes",nodes);
		return "/frame";
	}
	
	@RequestMapping(value = "{template}/model/add", method = { RequestMethod.POST })
	@ResponseBody
	public String add(@PathVariable String template,HttpServletRequest request) {
		String node = request.getParameter("name");
		kvDB.updateTemplate(template,KVDB.MODEL,node,"");
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", "");
		return json.toJSONString();
	}
	
}
