package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

public class MindMap {
	public long mindMapId;
	public String name;
	public List<Node> nodeList;
	MindMapDao mindMapDao = new MindMapDao();
	NodeDao nodeDao = new NodeDao();
	public List<Node> getNodes() {
		return nodeList;
	}

	public void addNode(Node node) {
		if (nodeList == null) {
			nodeList = new ArrayList<Node>();
		}
		node.mindMapId = mindMapId;

		// ¼ÆËãnode×ø±ê
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
			// double angleR = 180f * angle / Math.PI;
			double x = Math.cos(angle) * 300;
			double y = Math.sin(angle) * 300;
			node.x = node.parentNode.x + Math.round(x);
			node.y = node.parentNode.y + Math.round(y);
		}

		long id = nodeDao.insert(node);
		node._id = id;
		nodeList.add(node);
	}

	public boolean hasNode() {
		return nodeList != null && nodeList.size() > 0;
	}

	public double log2(double value) {
		return Math.log(value) / Math.log(2.0);

	}

	public List<Node> removeNode(Node node) {
		List<Node> listNode = nodeDao.getNodesBy(node._id, node.isRootNode);
		nodeList.removeAll(listNode);
		nodeDao.deleteNodesBy(node._id, node.isRootNode);
		return listNode;
	}

	public void updateNodeTile(Node node) {
		nodeDao.update(node);
		if(node.isRootNode){
			name = node.title;
			mindMapDao.updateMindMapnnName(mindMapId,name);
		}
	}

	public void updateNodeLocation(Node node) {
		nodeDao.update(node);
	}
}
