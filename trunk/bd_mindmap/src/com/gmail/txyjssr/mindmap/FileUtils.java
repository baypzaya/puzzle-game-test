package com.gmail.txyjssr.mindmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

	public static boolean writeToFile(String string, String path, String fileName) {

		File direct = new File(path);
		if (!direct.exists()) {
			direct.mkdirs();
		}
		File file = new File(direct, fileName);
		FileOutputStream out = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			out.write(string.getBytes());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String readFile(String path, String fileName) {
		byte[] t = new byte[1024];
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(path, fileName);
			if (file.exists()) {
				FileInputStream readFile = new FileInputStream(file);
				int count = 0;
				while ((count = readFile.read(t)) > 0) {
					String s = new String(t, 0, count);
					sb.append(s);
				}
				readFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static File createFile(String filePath, String fileName) {
		try {
			File newFile = null;
			boolean result = true;
			File dir = new File(filePath);
			if (!dir.exists()) {
				result = dir.mkdirs();
			}

			if (result) {
				newFile = new File(filePath, fileName);
				if (!newFile.exists()) {
					result = newFile.createNewFile();
				}
			}
			
			if(result){
				return newFile;
			}else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String joinFilePath(String filePath, String fileName) {
		String result = null;
		if (!filePath.endsWith("/")) {
			result = filePath + "/";
		} else {
			result = filePath;
		}

		result += fileName;
		return result;
	}

	public static String getExtension(String path) {
		String extension = null;
		if (path != null && (path.indexOf(".") > 0)) {
			String[] strs = path.split("\\.");
			extension = strs[strs.length - 1];
		}
		return extension;
	}

	public static CharSequence getName(String path) {
		String[] str = path.split(File.separator);
		if (str != null && str.length > 0) {
			return str[str.length - 1];
		}
		return null;
	}
}
