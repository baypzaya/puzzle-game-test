package com.gmail.txyjssr.reader.data;

import java.io.Serializable;

public class Book implements Serializable {

	private static final long serialVersionUID = 1603954868050273010L;

	public long id;
	public String name;
	public String path;
	public int progress;
	public long lastReadTime;
	public String encodeType;

}
