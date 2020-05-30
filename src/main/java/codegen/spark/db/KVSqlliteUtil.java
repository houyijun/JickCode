package codegen.spark.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 創建KV數據庫到Sqllite
 * 
 * @author Yijun Hou
 *
 */
public class KVSqlliteUtil {

	private static final Logger LOG = LoggerFactory.getLogger(KVSqlliteUtil.class);
	
	/**
	 * 生成一个DB文件 （自动创建，创建连接）
	 * 
	 * @param filePath
	 *            生成文件夹的路径 如：C:/fileTest/aabb.db
	 * @return 当前DB文件的连接通道
	 */
	public static Connection connectDB(String filePath) {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + filePath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return con;
	}

	public static boolean createTable(Connection con,  String tableName) {
		try {
			Statement stat = con.createStatement();
			stat.executeUpdate("create table " + tableName + " (key TEXT, value TEXT); ");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean createTemplateTable(Connection con,  String tableName) {
		try {
			Statement stat = con.createStatement();
			stat.executeUpdate("create table " + tableName + " (template TEXT,key TEXT, value TEXT); ");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isTableExist(Connection con, String tableName) {
		String sql = "SELECT COUNT(*) as count FROM sqlite_master where type='table' and name='" + tableName + "';";
		Integer count = 0;
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			// 执行查询语句
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				count = rs.getInt("count");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count > 0;
	}
	

	public static void deleteTable(Connection con, String tableName) {
		try {
			Statement stat = con.createStatement();
			if (tableName != null && !"".equals(tableName)) {
				stat.executeUpdate("drop table if exists " + tableName + ";");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteKey(Connection con, String tableName, String key) {
		try {
			Statement stat = con.createStatement();
			if (tableName != null && !"".equals(tableName)) {
				stat.executeUpdate("delete from " + tableName + " where key='" + key + "';");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add KV to tableName
	 * 
	 * @param con
	 * @param tableName
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean addKV(Connection con, String tableName, String key, String value) {
		value=value.replaceAll("'","\'");
		PreparedStatement prep = null;
		try {
			prep = con.prepareStatement("insert into " + tableName + " values (?, ?);");
			prep.setString(1, key);
			prep.setString(2, value);
			prep.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean rename(Connection con, String tableName, String oldKey, String newKey) {
		newKey=newKey.replaceAll("'","\'");
		PreparedStatement prep = null;
		try {
			prep = con.prepareStatement("update " + tableName + " set key='"+newKey+"' where key='"+oldKey+"';");
			prep.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static boolean updateKVValue(Connection con, String tableName, String key, String value) {
		value=value.replaceAll("'","\'");
		String sql = "update " + tableName + " set value='" + value + "' where key='" + key + "';";
		Statement prep = null;
		try {
			prep = con.createStatement();
			prep.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Map<String, String> getAllKVs(Connection connection, String tableName)  {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			ResultSet rs = statement.executeQuery("select key,value from " + tableName + ";");
			while (rs.next()) {
				String key = rs.getString("key");
				String value = rs.getString("value");
				map.put(key, value);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static List<String> getKeys(Connection connection, String tableName)  {
		List<String> keys = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			ResultSet rs = statement.executeQuery("select key from " + tableName + ";");
			while (rs.next()) {
				String key = rs.getString("key");
				if (key!=null) {
					keys.add(key);
				}
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keys;
	}

	public static String getKeyValue(Connection connection, String tableName, String key) {
		String retValue = null;
		try {
			String sql = "select key,value from " + tableName + " where key='" + key + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				retValue = rs.getString("value");
				break;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	
	/**
	 * 模板
	 */

	public static void deleteTemplateKey(Connection con,String template, String tableName, String key) {
		try {
			Statement stat = con.createStatement();
			if (tableName != null && !"".equals(tableName)) {
				stat.executeUpdate("delete from " + tableName + " where key='" + key + "' and template='"+template+"';");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add KV to tableName
	 * 
	 * @param con
	 * @param tableName
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean addTemplateKV(Connection con, String template,String tableName, String key, String value) {
		value=value.replaceAll("'","\'");
		PreparedStatement prep = null;
		try {
			prep = con.prepareStatement("insert into " + tableName + " values (?,?, ?);");
			prep.setString(1,template);
			prep.setString(2, key);
			prep.setString(3, value);
			prep.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static boolean renameTemplateKV(Connection con, String template,String tableName, String oldKey, String newKey) {
		newKey=newKey.replaceAll("'","\'");
		PreparedStatement prep = null;
		try {
			prep = con.prepareStatement("update " + tableName + " set key='"+newKey+"' where key='"+oldKey+"' and template='"+template+"'");
			prep.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean updateTemplateKVValue(Connection con,String template, String tableName, String key, String value) {
		String newValue=value.replace("'","''");
		String sql = "update " + tableName + " set value='" + newValue + 
				"' where key='" + key + "' and template='"+template+"';";
		LOG.info("KVSqlite:{}",sql);
		Statement prep = null;
		try {
			prep = con.createStatement();
			prep.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static Map<String, String> getTemplateAllKVs(Connection connection,String template, String tableName)  {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			ResultSet rs = statement.executeQuery("select key,value from " + tableName + " where  template='"+template+"';");
			while (rs.next()) {
				String key = rs.getString("key");
				String value = rs.getString("value");
				map.put(key, value);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static List<String> getTemplateKeys(Connection connection, String template,String tableName)  {
		List<String> keys = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			ResultSet rs = statement.executeQuery("select key from " + tableName + " where  template='"+template+"';");
			while (rs.next()) {
				String key = rs.getString("key");
				if (key!=null) {
					keys.add(key);
				}
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keys;
	}

	public static String getTemplateKeyValue(Connection connection,String template, String tableName, String key) {
		String retValue = null;
		try {
			String sql = "select key,value from " + tableName + " where key='" + key +"' and template='"+template+"';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				retValue = rs.getString("value");
				break;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retValue;
	}
}
