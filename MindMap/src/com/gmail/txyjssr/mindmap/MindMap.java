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
	}

	public boolean hasNode() {
		return nodeList != null && nodeList.size() > 0;
	}

}
