package codegen.spark.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codegen.spark.db.KVDB;

/**
 * 配置代码生成器的模板：spark,camel等等类似的
 * 1）每套模板在前端是一个单独的菜单和url路径。
 * 2）每套模板配置自己的jnode，配置jnode的dialog和生成ftl规则。
 * 3）每套模板只允许一套输出代码的ftl文件，其中jnode流程图生成一个${generated_code}变量。
 * @author Yijun Hou
 *
 */
@Service
public class MyConfig {
	
	@Autowired
	KVDB kvDB;

}
