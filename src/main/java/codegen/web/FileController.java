package codegen.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;
import codegen.spark.model.SVG;
import codegen.spark.model.SVGNode;
import codegen.spark.service.FileService;
import codegen.spark.service.JickCodeService;
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

	public static String path = "c:/github/houyijun";

	@Autowired
	KVDB kvDB;

	@Autowired
	JickCodeService jickCodeService;

	@Autowired
	FileService fileService;

	@RequestMapping(value = { "export/{svg}/{codetype}" })
	public String export(@PathVariable String svg, @PathVariable String codetype, Map<String, Object> map) {
		map.put("divname", "/file/export.ftl");
		map.put("codetype", codetype);
		map.put("svg", svg);

		String svgJson =kvDB.get(KVDB.SVG,svg);
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
			code = FreeMakerUtil.outFtl(codemap, FileController.path, codetype + FileService.SUFFIX);

		} catch (Exception e) {
			LOG.error("输出代码异常:{}", e);
			e.printStackTrace();
			code = "output code exception!";
		}

		map.put("code", code);
		return "/frame";
	}

	@RequestMapping(value = { "importcode" })
	public String importCode(Map<String, Object> map) {
		map.put("divname", "/file/import.ftl");
		return "/frame";
	}

	// 导入
	@RequestMapping("importcode.do")
	@ResponseBody
	public String importcode(@RequestParam(value = "filename") MultipartFile file, HttpServletRequest request) {
		try {
			String str = new String(file.getBytes());
			LOG.info("上传内容:{}", str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}

	@RequestMapping("exportcode")
	public void exportCode(HttpServletResponse response) {
		Funcs.exportCodeFile(response, "example.code","hello code 22");
	}

}
