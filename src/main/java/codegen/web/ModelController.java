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
@RequestMapping("/model")
public class ModelController {
	private static final Logger LOG = LoggerFactory.getLogger(ModelController.class);

	@Autowired
	KVDB kvDB;
	
	@RequestMapping(value = { "import" })
	public String importModel(Map<String, Object> map) {
		map.put("divname", "/model/import.ftl");
		return "/frame";
	}

	// 导入
	@RequestMapping("import.do")
	@ResponseBody
	public String importModel_do(@RequestParam(value = "filename") MultipartFile file, HttpServletRequest request) {
		try {
			String modelname = request.getParameter("name");
			String modelfile = new String(file.getBytes());
			String modelcontent= request.getParameter("modelcontent");
			LOG.info("上传名称={},文件内容={}",modelname,modelfile);
			kvDB.saveOrUpdate(KVDB.MODEL,modelname,modelfile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("exportcode")
	public void exportCode(HttpServletResponse response) {
		String outFile="example.code";
		Funcs.exportCodeFile(response,outFile,"hello code" );
	}
	
	/**
	 * 显示模板内容
	 * 
	 * @param modelname
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "edit/{modelname}" })
	public String edit(@PathVariable String modelname,Map<String, Object> map) {
		map.put("divname", "/model/edit.ftl");
		map.put("modelname", modelname);
		map.put("modelcontent",kvDB.get(KVDB.MODEL,modelname));
		return "/frame";
	}
	
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request) {
		String node = request.getParameter("name");
		boolean success=kvDB.del(KVDB.MODEL,node);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}
	
	@RequestMapping(value = { "all" })
	public String all(Map<String, Object> map) {
		map.put("divname", "/model/all.ftl");
		List<String> nodes= kvDB.getKeys(KVDB.MODEL);
		map.put("nodes",nodes);
		return "/frame";
	}
	
	@RequestMapping(value = { "new" })
	public String newModel(Map<String, Object> map) {
		map.put("divname", "/model/edit.ftl");
		return "/frame";
	}
	

}
