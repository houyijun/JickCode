package codegen.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import codegen.spark.service.StorageService;
/**
 * iterate json object $.each(anObject,function(name,value) { alert(name);
 * alert(value); });
 * 
 * 
 * @author Yijun Hou
 *
 */
@Controller
@EnableAutoConfiguration
@ComponentScan(basePackages = { "codegen" })
@SpringBootApplication
public class TestController {
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

	@Autowired
	StorageService storageService;

	private JSONObject getJsonResp(String code, String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		if (data != null) {
			json.put("data", data);
		}
		return json;
	}

	@RequestMapping(value = { "/" })
	public String home(Map<String, Object> map) {
		return "home";
	}
	
	@RequestMapping(value = "/getjnode", method = { RequestMethod.GET })
	@ResponseBody
	public String getjnode(String jnode) {
		String json = storageService.getJNode(jnode);
		LOG.info("###getjnode name={},value={}", jnode, json);
		return json;
	}


	/**
	 * upload svg data
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/postSvg", method = { RequestMethod.POST })
	@ResponseBody
	public String postSvg(HttpServletRequest request) {
		String svgName = request.getParameter("svgName");
		String svg = request.getParameter("svg");
		LOG.info("###post svgname={},svg={}", svgName, svg);
		storageService.putSvgJson(svgName, svg);

		JSONObject json = getJsonResp("0", "SUCCESS", null);
		return json.toJSONString();
	}
	
	

	/**
	 * get svg data
	 * 
	 * @param svgName
	 * @return
	 */
	@RequestMapping(value = "/getSvg", method = { RequestMethod.GET })
	@ResponseBody
	public String getSvg(String svgName) {
		String json = storageService.getSvgJson(svgName);
		LOG.info("###query svgname={},svg={}", svgName, json);
		return json;
	}


	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestController.class, args);
	}
}
