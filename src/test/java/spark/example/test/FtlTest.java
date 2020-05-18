package spark.example.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codegen.spark.utils.FreeMakerUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FtlTest extends TestCase{
	private static final Logger LOG = LoggerFactory.getLogger(FtlTest.class);


	public static Test suite() {
		return new TestSuite(FtlTest.class);
	}

	public FtlTest(String testName) {
		super(testName);
	}

	public void testMe() throws Exception{
		String templatefile = "src/main/resources/model/spark.ftl";
		String toCode = "test Code";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code_generated", toCode);
		String str = FreeMakerUtil.outFtl(map, templatefile);
		LOG.info("#ftl:{}",str);
		
		LOG.info("now write ftl to file");
		String outfile = "src/main/resources/class/Example.scala";
		FreeMakerUtil.Ftl2File(map, templatefile, outfile);

	}
}
