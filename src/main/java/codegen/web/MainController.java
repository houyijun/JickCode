package codegen.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	private static final Logger LOG = LoggerFactory.getLogger(MainController.class);


	@RequestMapping(value = { "/" })
	public String home(Map<String, Object> map) {
		LOG.info("JickCode home page");
		return "redirect:/project/all";
	}
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainController.class, args);
	}
}
