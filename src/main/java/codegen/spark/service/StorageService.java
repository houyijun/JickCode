package codegen.spark.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;

import codegen.spark.db.KVDB;

/**
 * KV 存储服务，存储两种数据：svg配置图信息、和jnode节点配置信息
 * 
 * @author Yijun Hou
 *
 */
@Service
public class StorageService {
	private static final Logger LOG = LoggerFactory.getLogger(StorageService.class);

	@Autowired
	KVDB kvDB;

	/**
	 * 用户流程图配置
	 * 
	 * @param svgName
	 * @return
	 */
	public String getSvgJson(String svgName) {
		String value = kvDB.get(KVDB.SVG,svgName);
		JSONObject json = new JSONObject();
		json.put("code", "0");
		json.put("msg", "SUCCESS");
		if (value != null) {
			json.put("data", value);
		} else {
			json.put("data", "no svg");
		}
		return json.toJSONString();
	}

	public void putSvgJson(String svnName, String svnJson) {
		JSONObject jsonobject = JSONObject.parseObject(svnJson);
		LOG.info("###svnJson:{}", jsonobject.toJSONString());
		kvDB.saveOrUpdate(KVDB.SVG,svnName, svnJson);
	}
	
	public List<String> getSvgKeys(){
		List<String > keys = kvDB.getKeys(KVDB.SVG);
		return keys;
	
	}

	
	public String querySvgJson(String svgName) {
		String value = kvDB.get(KVDB.SVG,svgName);
		return value;
	}
	
	public boolean delSvgNode(String name) {
		return kvDB.del(KVDB.SVG,name);
	}
	/**
	 * JNode节点配置信息管理 格式： 名称-> json字符串
	 */
	public String getJNode(String jnodeName) {
		String value = kvDB.get(KVDB.JNODE,jnodeName);
		return value;
	}

	public void putJNode(String jnodeName, String jnodeData) {
		kvDB.saveOrUpdate(KVDB.JNODE,jnodeName, jnodeData);
	}
	
	public boolean delJNode(String jnodeName) {
		return kvDB.del(KVDB.JNODE,jnodeName);
	}
	
	public List<String> getJNodeKeys(){
		List<String > keys = kvDB.getKeys(KVDB.JNODE);
		return keys;	
	}
	
	public Map<String,String> getAllJNodes(){
		Map<String,String> map = kvDB.getAll(KVDB.JNODE);
		return map;	
	}

}
