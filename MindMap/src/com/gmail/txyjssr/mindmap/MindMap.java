package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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

		// ����node����
		if (node.parentNode != null) {
			int childCount = node.parentNode.getChildCount();
			double result = log2(childCount);
			int unit = (int) result;
			if (unit != result) {
				unit += 1;
			}

			double angle = 0;
			if (childCount > 1) {
				if (node.parentNode.isRootNode) {
					angle = 2 * Math.PI / Math.pow(2, unit) * (2 * (childCount - Math.pow(2, unit - 1)) - 1);
				} else {
					angle = Math.PI / Math.pow(2, unit) * (2 * (childCount - Math.pow(2, unit - 1)) - 1);
				}
			}

			if (!node.parentNode.isRootNode) {
				double xT = node.parentNode.x - node.parentNode.parentNode.x;
				double yT = node.parentNode.y - node.parentNode.parentNode.y;

				double angleT = 0;
				if (yT == 0) {
					if(node.parentNode.x > node.parentNode.parentNode.x){
						angleT = 0 ;
					}else if(node.parentNode.x < node.parentNode.parentNode.x){
						angleT = Math.PI ;
					}
				}else if(xT == 0){
					if(node.parentNode.y > node.parentNode.parentNode.y){
						angleT = Math.PI/2 ;
					}else if(node.parentNode.y < node.parentNode.parentNode.y){
						angleT = -Math.PI/2 ;
					}
				} else {
					angleT = Math.atan(xT / yT);
					if(node.parentNode.x < node.parentNode.parentNode.x){
						angleT = Math.PI+angleT ;
					}
				}
				// log code start
				double angleRT = 180f * angleT / Math.PI;

				Log.i("yujsh log", "angleRT:" + angleRT);
				// log code end
//				angle = angle - angleT;
				angle = angleT;
			}

			// log code start
			double angleR = 180f * angle / Math.PI;

			Log.i("yujsh log", "angleR:" + angleR);
			// log code end

			double x = Math.cos(angle) * 180;
			double y = Math.sin(angle) * 180;
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
		List<Node> listNode = getAllTreeNodes(node);
		if (!node.isRootNode) {
			listNode.add(node);
			node.parentNode.nodeChildren.remove(node);
		}else{
			node.nodeChildren.clear();
		}
		nodeList.removeAll(listNode);
		nodeDao.deleteNodesBy(listNode);
		return listNode;
	}

	private List<Node> getAllTreeNodes(Node node) {
		List<Node> listNode = nodeDao.getChildNodesBy(node._id);
		List<Node> tempList = new ArrayList<Node>();
		for (Node n : listNode) {
			List<Node> tlistNode = nodeDao.getChildNodesBy(n._id);
			if (tlistNode.size() > 0) {
				tempList.addAll(getAllTreeNodes(n));
			}
		}
		listNode.addAll(tempList);
		return listNode;
	}

	public void updateNodeTile(Node node) {
		nodeDao.update(node);
		if (node.isRootNode) {
			name = node.title;
			mindMapDao.updateMindMapnnName(mindMapId, name);
		}
	}

	public void updateNodeLocation(Node node) {
		nodeDao.update(node);
	}

	public Node getRootNode() {
		for(Node n : nodeList){
			if(n.isRootNode){
				return n;
			}
		}
		return null;
	}
}
