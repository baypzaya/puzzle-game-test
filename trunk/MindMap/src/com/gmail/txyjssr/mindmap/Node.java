package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public int id;
	public String title = "";
	public float x;
	public float y;
	public Node parentNode;
	private List<Node> nodeChildren = new ArrayList<Node>();
	public boolean isRootNode;

	public void addChild(Node childNode) {
		nodeChildren.add(childNode);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node another = (Node) o;
			return this.id == another.id;
		}
		return false;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
		parentNode.addChild(this);
	}

	public int getChildCount() {
		return nodeChildren.size();
	}

}
