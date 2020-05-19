package codegen.spark.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileService {

	// 代码模板文件的后缀名
	public static String SUFFIX = ".jick";

	public List<String> getTemplateFiles(String rootPath,String suffix) {
		List<String> files = new ArrayList<String>();
		File[] fileList = new File(rootPath).listFiles();
		for (int i = 0; i < fileList.length; i++) {
			String file = fileList[i].getName();
			if (file.endsWith(suffix)) {
				files.add(file.substring(0,file.length()-suffix.length()));
			}
		}
		return files;
	}
}
