package com.gmail.txyjssr.reader.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileLogic {
	public List<File> getAllFiles(File currentFile) {
		List<File> fileList = null;
		File[] files = currentFile.listFiles();
		if (files != null) {
			fileList = Arrays.asList(files);
		} else {
			fileList = new ArrayList<File>();
		}
		return fileList;
	}
}
