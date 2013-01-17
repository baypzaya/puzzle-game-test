package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

public class MindMap {
	private List<Node> nodeList;

	public List<Node> getNodes() {
		return nodeList;
	}

	public void addNode(Node node) {
		if (nodeList == null) {
			nodeList = new ArrayList<Node>();
		}
		nodeList.add(node);

		if (node.parentNode != null) {
			node.x = node.parentNode.x-50;
			node.y = node.parentNode.y-50;
		}
	}

	public boolean hasNode() {
		return nodeList != null && nodeList.size() > 0;
	}

}
