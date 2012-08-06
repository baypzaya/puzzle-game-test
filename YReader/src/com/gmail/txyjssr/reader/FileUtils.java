package com.gmail.txyjssr.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileUtils {

	/**
	 * 获取文件编码格式
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getFileEncodeType(File file) {
		String encode = "GBK";

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);
			in.mark(4);
			byte[] first3bytes = new byte[3];
			in.read(first3bytes);
			in.reset();
			in.close();
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {
				encode = "utf-8";
			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
				encode = "unicode";
			} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
				encode = "utf-16be";
			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
				encode = "utf-16le";
			}
		} catch (Exception e) {
			e.printStackTrace();
			encode = "GBK";
		}
		return encode;
	}
}
