package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

import com.gmail.txyjssr.mindmap.db.BaseData;

public class Node extends BaseData{

//	public int id;
	public String title = "";
	public float x;
	public float y;
	public boolean isRootNode;
	public long parentNodeId;
	public long mindMapId;
	
	
	public Node parentNode;
	public List<Node> nodeChildren = new ArrayList<Node>();
	
	public void addChild(Node childNode) {
		nodeChildren.add(childNode);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node another = (Node) o;
			return this._id == another._id;
		}
		return false;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
		this.parentNodeId = parentNode._id;
		parentNode.addChild(this);
	}

	public int getChildCount() {
		return nodeChildren.size();
	}
	
	public int getLayerNum(){
		int i = 1;
		if(parentNode!=null){
			i+=parentNode.getLayerNum();
		}
		return i;
	}
}
