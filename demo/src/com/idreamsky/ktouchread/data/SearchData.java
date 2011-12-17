package com.idreamsky.ktouchread.data;

import java.io.File;
import java.io.Serializable;

public class SearchData implements Serializable {
	private File file;
	private int count;
	private boolean isDirectoryOrFile;  //true 目录 false 文件
	
	public boolean isDirectoryOrFile() {
		return isDirectoryOrFile;
	}

	public void setDirectoryOrFile(boolean isDirectoryOrFile) {
		this.isDirectoryOrFile = isDirectoryOrFile;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
