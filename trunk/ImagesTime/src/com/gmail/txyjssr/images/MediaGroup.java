package com.gmail.txyjssr.images;

import java.util.ArrayList;
import java.util.List;

public class MediaGroup {
	List<MediaInfo> mediaInfoList = new ArrayList<MediaInfo>();
	long id;
	String name;

	@Override
	public boolean equals(Object o) {
		boolean result;
		if (o instanceof MediaGroup) {
			MediaGroup mg = (MediaGroup) o;
			result = (id == mg.id);
		} else {
			result = false;
		}
		return result;
	}

	public void addItem(MediaInfo mi) {
		int index = mediaInfoList.indexOf(mi);
		if (index >= 0) {
			mediaInfoList.add(index, mi);
		} else {
			mediaInfoList.add(mi);
		}
	}
}
