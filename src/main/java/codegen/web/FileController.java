package codegen.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import codegen.spark.service.FileService;
import codegen.spark.service.JickCodeService;
import codegen.spark.service.StorageService;
import codegen.spark.utils.FreeMakerUtil;

/**
 * template files manager
 * 
 * @author houyiju
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {
	private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

	public static String path="c:/github/houyijun";
	
	
	@Autowired
	StorageService storageService;
	
	
	@Autowired
	JickCodeService jickCodeService;
	
	@Autowired
	FileService fileService;
	
	@RequestMapping(value = { "export/{svg}/{codetype}" })
	public String export(@PathVariable String svg,@PathVariable String codetype, Map<String, Object> map) {
		map.put("divname", "/file/export.ftl");
		map.put("codetype", codetype);
		map.put("svg", svg);
		
		String svgJson = storageService.querySvgJson(svg);
		JSONObject json = JSONObject.parseObject(svgJson);
		JSONArray chart = (JSONArray) json.get("chart");

		List<SVGNode> nodes = chart.toJavaList(SVGNode.class);
		SVG svgobj = new SVG();
		svgobj.setParams(new HashMap<String, String>());
		svgobj.setNodes(nodes);
		String code = "";
		try {
			code = jickCodeService.toCode(svgobj);
			
			Map<String,Object> codemap =new HashMap<String,Object>();
			codemap.put("code_generated",code);
			code=FreeMakerUtil.outFtl(codemap, FileController.path,codetype+FileService.SUFFIX);
			
		} catch (Exception e) {
			LOG.error("输出代码异常:{}", e);
			e.printStackTrace();
			code = "output code exception!";
		}
		
		map.put("code", code);
		return "/frame";
	}

	@RequestMapping(value = "test", method = { RequestMethod.GET })
	@ResponseBody
	public String test(HttpServletRequest request) {
		doit(request);
		return "test";
	}

	private void doit(HttpServletRequest request) {
		String path = "C:\\github";
		List<String> filenames = fileService.getTemplateFiles(path, ".zip");
		LOG.info("#ROOT:{}", path);
		for (String name : filenames) {
			LOG.info("#subfile:{}", name);
		}
	}

}
