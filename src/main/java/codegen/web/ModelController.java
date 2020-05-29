package codegen.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

	// 导入模板文件
	@RequestMapping("{template}/model/import.do")
	public String importModel_do(@PathVariable String template, @RequestParam(value = "filename") MultipartFile file,
			HttpServletRequest request) {
		try {
			String modelfile = new String(file.getBytes());
			kvDB.updateTemplate(template, KVDB.MODEL, template, modelfile);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("导入代码模板异常:{}", e);
		}
		return "redirect:/" + template + "/model/edit";
	}

	// 导入模板文本
	@RequestMapping("{template}/model/importContent.do")
	public String importModelContent_do(@PathVariable String template,
			@RequestParam(value = "modelContent") String modelContent, HttpServletRequest request) {
		try {
			kvDB.updateTemplate(template, KVDB.MODEL, template, modelContent);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("导入代码文本异常:{}", e);
		}
		return "redirect:/" + template + "/model/edit";
	}

	/**
	 * 下载模板文件内容
	 * 
	 * @param name
	 * @param response
	 */
	@RequestMapping("{template}/model/download")
	public void download(@PathVariable String template, String name, HttpServletResponse response) {
		String outFile = name + ".txt";
		String modelcontent = kvDB.getTemplate(template, KVDB.MODEL, name);
		if (modelcontent == null) {
			modelcontent = "null";
		}
		Funcs.exportCodeFile(response, outFile, modelcontent);
	}

	/**
	 * 显示模板内容
	 * 
	 * @param modelname
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "{template}/model/edit" })
	public String edit(@PathVariable String template, Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/model/edit.ftl");
		map.put("modelcontent", kvDB.getTemplate(template, KVDB.MODEL, template));
		return "/frame";
	}

}
