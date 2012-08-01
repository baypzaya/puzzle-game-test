package com.gmail.txyjssr.reader;

import java.io.Serializable;

public class Book implements Serializable {
	
	private static final long serialVersionUID = 1603954868050273010L;
	
	public String name;
	public String path;
	public long progress;
	public long total;

}
