package codegen.spark.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;
@Service
public class KVDB {
	
	public static  final  String SVG="spark";
	public static  final String JNODE="jnode";
	private final String dbFilePath = "C:\\github\\data\\spark.db";
	
	
	private Connection con = null;

	@PostConstruct
	public void open() {
		if (con == null) {
			con = KVSqlliteUtil.connectDB(dbFilePath);
		}
		if (con!=null) {
			if (!KVSqlliteUtil.isTableExist(con, SVG)) {
				KVSqlliteUtil.createTable(con, SVG);
			}
			if (!KVSqlliteUtil.isTableExist(con, JNODE)) {
				KVSqlliteUtil.createTable(con, JNODE);
			}
		}

	}

	@PreDestroy
	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveOrUpdate(String tableName,String key, String value) {
		String oldvalue = KVSqlliteUtil.getKeyValue(con, tableName, key);
		if (oldvalue == null) {
			KVSqlliteUtil.addKV(con, tableName, key, value);
		} else {
			KVSqlliteUtil.updateKVValue(con, tableName, key, value);
		}
	}

	public Map<String, String> getAll(String tableName) {
		Map<String, String> map = KVSqlliteUtil.getAllKVs(con, tableName);
		return map;
	}

	public String get(String tableName,String key) {
		String value = KVSqlliteUtil.getKeyValue(con, tableName, key);
		return value;
	}
	
	public boolean del(String tableName,String key) {
		 KVSqlliteUtil.deleteKey(con, tableName, key);
		return true;
	}
	
	public List<String> getKeys(String tableName){
		List<String > keys = KVSqlliteUtil.getKeys(con, tableName);
		return keys;
	
	}
	
	
}
