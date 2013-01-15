package com.gmail.txyjssr.mindmap;

public class LocationUtils {

	public Point getCenter(int left, int top, int right, int bottom) {
		Point p = new Point();
		p.x = (right - left) / 2L;
		p.y = (bottom - top) / 2L;
		return p;
	}
}
