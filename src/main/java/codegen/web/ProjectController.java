package codegen.web;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;
import codegen.spark.model.SVG;
import codegen.spark.model.SVGNode;
import codegen.spark.service.JickCodeService;
import codegen.spark.utils.FreeMakerUtil;

@Controller
public class ProjectController {
	private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	KVDB kvDB;

	@Autowired
	JickCodeService jickCodeService;

	@RequestMapping(value = { "{template}/project/all" })
	public String all(@PathVariable String template, Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/project/all.ftl");
		List<String> jnodeNames = kvDB.getTemplateKeys(template, KVDB.SVG);
		map.put("svglist", jnodeNames);
		return "/frame";
	}

	@RequestMapping(value = "{template}/project/delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(@PathVariable String template, HttpServletRequest request) {
		String node = request.getParameter("node");
		boolean success = kvDB.delTemplate(template, KVDB.SVG, node);

		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}

	@RequestMapping(value = "{template}/project/add.do", method = { RequestMethod.POST })
	@ResponseBody
	public String add(@PathVariable String template, HttpServletRequest request) {
		String name = request.getParameter("name");
		String svg = getSvgJson(template, name);
		if (svg == null) {
			return Funcs.getJsonResp("1", "duplicated svg name", null).toJSONString();

		}
		kvDB.updateTemplate(template, KVDB.SVG, name, "{\"chart\":\"\"}");
		JSONObject ret = Funcs.getJsonResp("0", "SUCCESS", null);
		return ret.toJSONString();
	}

	@RequestMapping(value = { "{template}/project/edit/{svgname}" })
	public String edit(@PathVariable String template, @PathVariable String svgname, Map<String, Object> map) {
		map.put("template", template);
		map.put("svgname", svgname);
		map.put("divname", "/project/edit.ftl");
		String json = getSvgJson(template, svgname);
		map.put("initdata", json);
		Map<String, String> nodes = kvDB.getTemplateAll(template, KVDB.JNODE);
		List<String> names = new ArrayList<String>();
		Map<String, String> modelMap = new HashMap<String, String>();
		for (String key : nodes.keySet()) {
			names.add(key);
			String modal = "";
			try {
				JSONObject obj = JSONObject.parseObject(nodes.get(key));
				modal = obj.getString("dialog");
			} catch (Exception e) {
				LOG.error("模板加载异常:{}", e);

			}
			modelMap.put(key, modal);
		}
		map.put("names", names);
		map.put("modals", modelMap);
		return "/frame";
	}

	/**
	 * upload svg data
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "{template}/project/postSvg", method = { RequestMethod.POST })
	@ResponseBody
	public String postSvg(@PathVariable String template, HttpServletRequest request) {
		String svgName = request.getParameter("svgName");
		String svg = request.getParameter("svg");
		kvDB.updateTemplate(template, KVDB.SVG, svgName, svg);
		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", null);
		return json.toJSONString();
	}

	/**
	 * get svg data
	 * 
	 * @param svgName
	 * @return
	 */
	@RequestMapping(value = "{template}/project/getSvg", method = { RequestMethod.GET })
	@ResponseBody
	public String getSvg(@PathVariable String template, String svgName) {
		String json = getSvgJson(template, svgName);
		if (json != null) {
			return json;
		} else {
			return "";
		}
	}

	// 导入模板文件
	@RequestMapping("{template}/project/import.do")
	public String import_do(@PathVariable String template, String name,
			@RequestParam(value = "filename") MultipartFile file, HttpServletRequest request) {
		try {
			String projectContent = new String(file.getBytes());
			kvDB.updateTemplate(template, KVDB.SVG, name, projectContent);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("导入代码模板异常:{}", e);
		}
		return "redirect:/" + template + "/project/all";
	}

	@RequestMapping(value = "{template}/project/download/{svg}")
	public void download(@PathVariable String template, @PathVariable String svg, HttpServletResponse response) {
		String outFile = svg + ".proj";
		String svgContent = kvDB.getTemplate(template, KVDB.SVG, svg);
		if (svgContent == null) {
			svgContent = "null";
		}
		Funcs.exportCodeFile(response, outFile, svgContent);
	}

	@RequestMapping(value = { "{template}/project/export/{svg}" })
	public String export(@PathVariable String template, @PathVariable String svg, Map<String, Object> map) {
		map.put("template", template);
		map.put("divname", "/project/export.ftl");
		map.put("svg", svg);
		String code = getCode(template, svg, template);
		map.put("code", code);
		return "/frame";
	}

	@RequestMapping(value = "{template}/project/exportcode/{svg}")
	public void exportCode(@PathVariable String template, @PathVariable String svg, HttpServletResponse response) {
		String outFile = svg + ".txt";
		String code = getCode(template, svg, template);
		if (code == null) {
			code = "null";
		}
		Funcs.exportCodeFile(response, outFile, code);
	}

	private String getCode(String template, String name, String type) {
		String svgJson = kvDB.getTemplate(template, KVDB.SVG, name);
		JSONObject json = JSONObject.parseObject(svgJson);
		if (!json.containsKey("chart") || StringUtils.isEmpty(json.getString("chart"))) {
			return "代码生成异常:" + svgJson;
		}
		JSONArray chart = (JSONArray) json.get("chart");

		List<SVGNode> nodes = chart.toJavaList(SVGNode.class);
		SVG svgobj = new SVG();
		svgobj.setParams(new HashMap<String, String>());
		svgobj.setNodes(nodes);
		String code = "";
		try {
			code = jickCodeService.toCode(template, svgobj);

			Map<String, Object> codemap = new HashMap<String, Object>();
			codemap.put("code_generated", code);

			String whole_ftl = kvDB.getTemplate(template, KVDB.MODEL, type);
			code = FreeMakerUtil.outStringFtl(codemap, whole_ftl);
		} catch (Exception e) {
			LOG.error("输出代码异常:{}", e);
			e.printStackTrace();
			code = "output code exception!";
		}
		return code;
	}

	private String getSvgJson(String template, String svgName) {
		String value = kvDB.getTemplate(template, KVDB.SVG, svgName);
		return Funcs.getJsonResp("0", "SUCCESS", value).toJSONString();
	}

}
