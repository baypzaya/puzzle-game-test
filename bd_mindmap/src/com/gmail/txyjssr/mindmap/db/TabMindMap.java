package com.gmail.txyjssr.mindmap.db;


public class TabMindMap extends BaseData{
	public String name;
	public boolean isCurrent;
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TabMindMap) {
			TabMindMap another = (TabMindMap) o;
			return this._id == another._id;
		}
		return false;
	}
	
}
