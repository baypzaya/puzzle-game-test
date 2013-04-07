package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

public class MindMap {
	public static final float DISTANCE_dEFAULT = 120; // 单位dp
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
		long id = nodeDao.insert(node);
		node._id = id;
		nodeList.add(node);
	}
	
	public void addNode(Node node,boolean isContainId) {
		if (nodeList == null) {
			nodeList = new ArrayList<Node>();
		}
		node.mindMapId = mindMapId;
		nodeDao.insert(node,isContainId);
		nodeList.add(node);
	}

	public void computeLocation(Node node) {
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
					int childCountT = childCount - 1;
					double temp = Math.pow(-1f, childCountT % 2) * (childCountT - childCountT / 2);
					angle = temp / 9f * Math.PI;
				}
			}

			if (!node.parentNode.isRootNode) {
				double xT = node.parentNode.x - node.parentNode.parentNode.x;
				double yT = node.parentNode.y - node.parentNode.parentNode.y;

				double angleT = 0;
				if (yT == 0) {
					if (node.parentNode.x > node.parentNode.parentNode.x) {
						angleT = 0;
					} else if (node.parentNode.x < node.parentNode.parentNode.x) {
						angleT = Math.PI;
					}
				} else if (xT == 0) {
					if (node.parentNode.y > node.parentNode.parentNode.y) {
						angleT = Math.PI / 2;
					} else if (node.parentNode.y < node.parentNode.parentNode.y) {
						angleT = -Math.PI / 2;
					}
				} else {
					angleT = Math.atan(yT / xT);
					if (node.parentNode.x < node.parentNode.parentNode.x) {
						angleT = Math.PI + angleT;
					}
				}
				angle = angleT + angle;
			} else {
				angle = 1 / 12f * Math.PI + angle;
			}

			double x = Math.cos(angle) * transformDP2PX(DISTANCE_dEFAULT);
			double y = Math.sin(angle) * transformDP2PX(DISTANCE_dEFAULT);
			node.x = node.parentNode.x + Math.round(x);
			node.y = node.parentNode.y + Math.round(y);
		}
	}

	private float transformDP2PX(float value) {
		return value * EngineApplication.sDensity;
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
			listNode.add(0, node);
			node.parentNode.nodeChildren.remove(node);
		} else {
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
		for (Node n : nodeList) {
			if (n.isRootNode) {
				return n;
			}
		}
		return null;
	}

	public void setNodeList(List<Node> nodes) {
		this.nodeList = nodes;
		orderNodeList(nodes);
	}

	public void orderNodeList(List<Node> nodeList) {
		Hashtable<Long, Node> ht = new Hashtable<Long, Node>();
		for (Node node : nodeList) {

			if (!ht.containsKey(node._id)) {
				ht.put(node._id, node);
			}

			if (node.parentNode != null) {
				continue;
			}

			if (ht.containsKey(node.parentNodeId)) {
				Node parentNode = ht.get(node.parentNodeId);
				node.setParentNode(parentNode);
			}
		}
	}
}
