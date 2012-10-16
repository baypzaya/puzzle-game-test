package com.gmail.txyjssr.game.data;

import java.util.ArrayList;
import java.util.List;

import com.wiyun.engine.types.WYPoint;

public class Path {
	float mTileWidth;
	float mTileHeight;

	List<WYPoint> pathList;

	public Path(float tileWidth, float tileHeight, int[] pathArray) {
		mTileWidth = tileWidth;
		mTileHeight = tileHeight;
		pathList = new ArrayList<WYPoint>(pathArray.length / 2);
		for (int i = 0; i < pathArray.length; i += 2) {
			float x = pathArray[i+1] * mTileWidth + mTileWidth / 2;
			float y = pathArray[i] * mTileHeight + mTileHeight / 2;
			WYPoint point = WYPoint.make(x, y);
			pathList.add(point);
		}
	}

	public WYPoint getFirstLocal() {
		return pathList.get(0);
	}

	public WYPoint getNextLocal(int currentIndex) {
		int index = currentIndex >= pathList.size() ? pathList.size() - 1 : currentIndex + 1;
		return pathList.get(index);
	}

	public boolean hasNext(int currentIndex) {
		return currentIndex+1 < pathList.size();
	}
}
