package codegen.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import codegen.spark.db.KVDB;

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
public class MainController {

	@Autowired
	KVDB kvDB;
	
	@RequestMapping(value = { "/" })
	public String home(Map<String, Object> map) {
		map.put("divname", "/template.ftl");
		List<String> templates= kvDB.getKeys();
		map.put("templates",templates);
		return "/frame";
	}
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainController.class, args);
	}
}
