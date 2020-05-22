package codegen.spark.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import codegen.spark.jnode.JNode;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreeMakerUtil {
	
	/**
	 * 从字符串导入ftl文本
	 * 
	 * @param beanMap
	 * @param templcate
	 * @return
	 * @throws Exception
	 */
	public static String outStringFtl(Map<String, Object> beanMap,String templcate) throws Exception{
		Configuration config = new Configuration();
		config.setObjectWrapper(new DefaultObjectWrapper());
		
		StringWriter stringWriter = new StringWriter();
		Template template = new Template("template", templcate, config);
		
		StringWriter writer = new StringWriter();
		template.process(beanMap, writer);
		writer.flush();
		String ret=writer.toString();
		writer.close();
		return ret;
	}
	
	public static String outFtl(Map<String, Object> beanMap,String rootPath,String file_ftl) throws Exception{
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File(rootPath));
		config.setObjectWrapper(new DefaultObjectWrapper());
		Template template = config.getTemplate(file_ftl, "UTF-8");
		StringWriter writer = new StringWriter();
		template.process(beanMap, writer);
		writer.flush();
		String ret=writer.toString();
		writer.close();
		return ret;
	}
	/**
	 *  ftl template output dest file
	 * @param beanMap
	 * @param file_ftl
	 * @param file_out
	 * @throws Exception
	 */
	public static void Ftl2File(Map<String, Object> beanMap,String rootPath,String file_ftl,String file_out) throws Exception{
//		Configuration config = new Configuration(Configuration.getVersion());
//		config.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File(rootPath));
		config.setObjectWrapper(new DefaultObjectWrapper());
		Template template = config.getTemplate(file_ftl, "UTF-8");
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_out), "UTF-8"));
		template.process(beanMap, out);
		out.flush();
		out.close();
	}
	
	public static void Ftl2File(JNode jickNode,String file_ftl,String file_out) throws Exception{
//		Configuration config = new Configuration(Configuration.getVersion());
//		config.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));
		Configuration config = new Configuration();
		config.setObjectWrapper(new DefaultObjectWrapper());
		Template template = config.getTemplate(file_ftl, "UTF-8");
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_out), "UTF-8"));
		template.process(jickNode, out);
		out.flush();
		out.close();
	}
}
