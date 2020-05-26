package codegen.spark.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;
@Service
public class KVDB {
	//模板文件名称，固定的，特殊的
	private static final String TEMPLATE="template";
	
	public static  final  String SVG="spark";
	public static  final String JNODE="jnode";
	public static  final String MODEL="model";
	
	private final String dbFilePath = "C:\\github\\data\\spark.db";
	
	
	private Connection con = null;

	@PostConstruct
	public void open() {
		if (con == null) {
			con = KVSqlliteUtil.connectDB(dbFilePath);
		}
		if (con!=null) {
			if (!KVSqlliteUtil.isTableExist(con, TEMPLATE)) {
				KVSqlliteUtil.createTable(con, TEMPLATE);
			}
			if (!KVSqlliteUtil.isTableExist(con, SVG)) {
				KVSqlliteUtil.createTemplateTable(con, SVG);
			}
			if (!KVSqlliteUtil.isTableExist(con, JNODE)) {
				KVSqlliteUtil.createTemplateTable(con, JNODE);
			}
			if (!KVSqlliteUtil.isTableExist(con, MODEL)) {
				KVSqlliteUtil.createTemplateTable(con, MODEL);
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

	public void updateTemplate(String template,String tableName,String key, String value) {
		String oldvalue = KVSqlliteUtil.getTemplateKeyValue(con,template, tableName, key);
		if (oldvalue == null) {
			KVSqlliteUtil.addTemplateKV(con,template, tableName, key, value);
		} else {
			KVSqlliteUtil.updateTemplateKVValue(con, template,tableName, key, value);
		}
	}
	
	/**
	 * 重命名
	 * @param template
	 * @param tableName
	 * @param oldKey
	 * @param newKey
	 */
	public void renameTemplateKey(String template,String tableName,String oldKey,String newKey) {
		KVSqlliteUtil.renameTemplateKV(con, template,tableName, oldKey,newKey);
	}

	public Map<String, String> getTemplateAll(String template,String tableName) {
		Map<String, String> map = KVSqlliteUtil.getTemplateAllKVs(con,template, tableName);
		return map;
	}

	public String getTemplate(String template,String tableName,String key) {
		String value = KVSqlliteUtil.getTemplateKeyValue(con, template,tableName, key);
		return value;
	}
	
	public boolean delTemplate(String template,String tableName,String key) {
		 KVSqlliteUtil.deleteTemplateKey(con,template, tableName, key);
		return true;
	}
	
	public List<String> getTemplateKeys(String template,String tableName){
		List<String > keys = KVSqlliteUtil.getTemplateKeys(con,template, tableName);
		return keys;
	
	}
	
	/**
	 * 模板管理  template
	 */
	public void update(String key, String value) {
		String oldvalue = KVSqlliteUtil.getKeyValue(con, TEMPLATE, key);
		if (oldvalue == null) {
			KVSqlliteUtil.addKV(con, TEMPLATE, key, value);
		} else {
			KVSqlliteUtil.updateKVValue(con, TEMPLATE, key, value);
		}
	}
	
	public void rename(String oldKey,String newKey) {
		KVSqlliteUtil.rename(con, TEMPLATE, oldKey,newKey);
	}

	public Map<String, String> getAll() {
		Map<String, String> map = KVSqlliteUtil.getAllKVs(con, TEMPLATE);
		return map;
	}

	public String get(String key) {
		String value = KVSqlliteUtil.getKeyValue(con, TEMPLATE, key);
		return value;
	}
	
	public boolean del(String key) {
		 KVSqlliteUtil.deleteKey(con, TEMPLATE, key);
		return true;
	}
	
	public List<String> getKeys(){
		List<String > keys = KVSqlliteUtil.getKeys(con, TEMPLATE);
		return keys;
	
	}
	
}
