package spark.example.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codegen.spark.db.KVDB;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SparkDBTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(SparkDBTest.class);

	private KVDB sparkDB=new KVDB();

	public static Test suite() {
		return new TestSuite(SparkDBTest.class);
	}

	public SparkDBTest(String testName) {
		super(testName);
	}

	public void testSparkDB() {
		
		
//		sparkDB.open();
//		String val="val ${node.props.myname} = spark.read.format(\"${node.props.myname2}\")\n"+
//				".load(\"${node.props.myname}\")\n";
//		sparkDB.saveOrUpdate(KVDB.JNODE,"sourceFile",val);
//		String v2=sparkDB.get(KVDB.JNODE,"sourceFile");
//		LOG.info("$$$$get jnode content:{}",v2);
//		sparkDB.close();

	}
	
}
