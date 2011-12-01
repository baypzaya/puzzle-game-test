package com.gmail.txyjssr.images;

import android.graphics.Bitmap;

public class MediaInfo {
	public static int TYPE_IMAGE = 0;
	public static int TYPE_VIDEO = 1;
	
	String id;
	String filePath;
	String name;
	int type;
	long createTime;
	long modifyTime;
	String thumbnailId;
	

	@Override
	public boolean equals(Object o) {
		boolean result;
		if (o instanceof MediaInfo) {
			MediaInfo mi = (MediaInfo) o;
			result = filePath.equals(mi.filePath);
		} else {
			result = false;
		}
		return result;
	}

}
