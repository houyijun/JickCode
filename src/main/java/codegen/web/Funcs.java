package codegen.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class Funcs {
	
	public static String readFileContent(String fileName) {
	    File file = new File(fileName);
	    BufferedReader reader = null;
	    StringBuffer sbf = new StringBuffer();
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String tempStr;
	        while ((tempStr = reader.readLine()) != null) {
	            sbf.append(tempStr);
	        }
	        reader.close();
	        return sbf.toString();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        }
	    }
	    return sbf.toString();
	}

	@SuppressWarnings("rawtypes")
	public static void exportCodeFile(HttpServletResponse response, String filename,String content) {
		OutputStream fos = null;
		try {
			// 响应输出流，让用户自己选择保存路径
			response.setCharacterEncoding("UTF-8");
			response.reset();// 清除缓存
//			response.setContentType("octets/stream");
			response.addHeader("Content-Disposition", "attachment;filename=" + filename);
			fos = response.getOutputStream();

			fos.write(content.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static  JSONObject getJsonResp(String code, String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		if (data != null) {
			json.put("data", data);
		}
		return json;
	}
}
