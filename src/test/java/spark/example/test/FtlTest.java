package spark.example.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
