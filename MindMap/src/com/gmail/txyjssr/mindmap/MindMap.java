package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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
			int childCount = node.parentNode.getChildCount();
			int unit = (int) log2(childCount)+1;
			int mod = childCount % unit;
			
			Log.i("yujsh log","childCount:"+childCount);
			Log.i("yujsh log","unit:"+unit+" mod:"+mod);
			
			double angle = 2 * Math.PI / unit * mod;
			double x = Math.cos(angle) * 100;
			double y = Math.sin(angle) * 100;

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
