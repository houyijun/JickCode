package codegen.web;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;
import codegen.spark.model.SVG;
import codegen.spark.model.SVGNode;
import codegen.spark.service.JickCodeService;
import codegen.spark.utils.FreeMakerUtil;

@Controller
@RequestMapping("/svg")
public class SvgController {
	private static final Logger LOG = LoggerFactory.getLogger(SvgController.class);
	
	@Autowired
	KVDB kvDB;
	
	@Autowired
	JickCodeService jickCodeService;


	@RequestMapping(value = { "all" })
	public String all(Map<String, Object> map) {
		map.put("divname", "/svg/all.ftl");
		List<String> jnodeNames =kvDB.getKeys(KVDB.SVG);
		map.put("svglist", jnodeNames);
		List<String> codetypes=kvDB.getKeys(KVDB.MODEL);
		map.put("codetypes", codetypes);
		return "/frame";
	}

	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public String delete(HttpServletRequest request) {
		String node = request.getParameter("node");
		boolean success =kvDB.del(KVDB.SVG,node);

		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", String.valueOf(success));
		return json.toJSONString();
	}

	@RequestMapping(value = "add.do", method = { RequestMethod.POST })
	@ResponseBody
	public String add(HttpServletRequest request) {
		String name = request.getParameter("name");
		String svg = getSvgJson(name);
		if (svg==null) {
			return Funcs.getJsonResp("1", "duplicated svg name", null).toJSONString();
			
		}
		kvDB.saveOrUpdate(KVDB.SVG,name, "{\"chart\":\"\"}");
		JSONObject ret = Funcs.getJsonResp("0", "SUCCESS", null);
		return ret.toJSONString();
	}

	@RequestMapping(value = { "edit/{svgname}" })
	public String edit(@PathVariable String svgname, Map<String, Object> map) {
		map.put("svgname", svgname);
		map.put("divname", "/svg/edit.ftl");
		Map<String, String> nodes =kvDB.getAll(KVDB.JNODE);
		List<String> names = new ArrayList<String>();
		Map<String,String> modelMap=new HashMap<String,String>();
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
	@RequestMapping(value = "postSvg", method = { RequestMethod.POST })
	@ResponseBody
	public String postSvg(HttpServletRequest request) {
		String svgName = request.getParameter("svgName");
		String svg = request.getParameter("svg");
		LOG.info("###postSvg name={},svg={}", svgName, svg);
		kvDB.saveOrUpdate(KVDB.SVG,	svgName, svg);

		JSONObject json = Funcs.getJsonResp("0", "SUCCESS", null);
		return json.toJSONString();
	}

	/**
	 * get svg data
	 * 
	 * @param svgName
	 * @return
	 */
	@RequestMapping(value = "getSvg", method = { RequestMethod.GET })
	@ResponseBody
	public String getSvg(String svgName) {
		String json = getSvgJson(svgName);
		LOG.info("###query svgname={},svg={}", svgName, json);
		if (json!=null) {
			return json;
		}else {
			return "";
		}
	}
	
	@RequestMapping("download")
	public void download(String name,String type,HttpServletResponse response) {
		String outFile=name+"."+type;
		String code = getCode(name,type);
		if (code==null) {
			code="null";
		}
		Funcs.exportCodeFile(response,outFile,code );
	}
	@RequestMapping(value = { "export/{svg}/{codetype}" })
	public String export(@PathVariable String svg, @PathVariable String codetype, Map<String, Object> map) {
		map.put("divname", "/svg/export.ftl");
		map.put("codetype", codetype);
		map.put("svg", svg);

		String code = getCode(svg,codetype);

		map.put("code", code);
		return "/frame";
	}
	
	private String getCode(String name,String type) {
		String svgJson =kvDB.get(KVDB.SVG,name);
		JSONObject json = JSONObject.parseObject(svgJson);
		JSONArray chart = (JSONArray) json.get("chart");

		List<SVGNode> nodes = chart.toJavaList(SVGNode.class);
		SVG svgobj = new SVG();
		svgobj.setParams(new HashMap<String, String>());
		svgobj.setNodes(nodes);
		String code = "";
		try {
			code = jickCodeService.toCode(svgobj);

			Map<String, Object> codemap = new HashMap<String, Object>();
			codemap.put("code_generated", code);
			
			String whole_ftl=kvDB.get(KVDB.MODEL,type);
			code = FreeMakerUtil.outStringFtl(codemap, whole_ftl);

		} catch (Exception e) {
			LOG.error("输出代码异常:{}", e);
			e.printStackTrace();
			code = "output code exception!";
		}
		return code;

	}

	
	private String getSvgJson(String svgName) {
		String value = kvDB.get(KVDB.SVG,svgName);
		JSONObject json = new JSONObject();
		json.put("code", "0");
		json.put("msg", "SUCCESS");
		if (value != null) {
			json.put("data", value);
		} else {
			json.put("data", "no svg");
		}
		return json.toJSONString();
	}
	
}
