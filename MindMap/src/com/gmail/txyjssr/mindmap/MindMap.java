package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class MindMap {
	private List<Node> nodeList;
	private int currentId = 0;

	public List<Node> getNodes() {
		return nodeList;
	}

	public void addNode(Node node) {
		if (nodeList == null) {
			nodeList = new ArrayList<Node>();
		}
		node.id = currentId;
		nodeList.add(node);
		currentId++;
		if (node.parentNode != null) {
			int childCount = node.parentNode.getChildCount();
			double result = log2(childCount);
			int unit = (int) result;
			if (unit != result) {
				unit += 1;
			}

			double angle = 0;
			if (childCount > 1) {
				angle = 2 * Math.PI / Math.pow(2, unit) * (2 * (childCount - Math.pow(2, unit - 1)) + 1);
			}
			double angleR = 180f * angle / Math.PI;
			Log.i("yujsh log", "angleR:" + angleR);
			double x = Math.cos(angle) * 150;
			double y = Math.sin(angle) * 150;
			node.x = node.parentNode.x + Math.round(x);
			node.y = node.parentNode.y + Math.round(y);
		}
	}

	public boolean hasNode() {
		return nodeList != null && nodeList.size() > 0;
	}

	public double log2(double value) {
		return Math.log(value) / Math.log(2.0);

	}
}
