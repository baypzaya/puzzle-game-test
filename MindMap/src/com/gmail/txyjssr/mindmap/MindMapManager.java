package com.gmail.txyjssr.mindmap;

import java.util.Hashtable;
import java.util.List;

public class MindMapManager {
	MindMapDao mindMapDao = new MindMapDao();
	NodeDao nodeDao = new NodeDao();

	public MindMap createMindMap() {
		TabMindMap tabMindMap = new TabMindMap();
		tabMindMap.name = "new mindmap";
		tabMindMap.isCurrent = true;
		long id = mindMapDao.insert(tabMindMap);

		Node node = new Node();
		node.isRootNode = true;
		node.parentNodeId = -1;
		node.x = 0;
		node.y = 0;
		node.title = "new mindmap";
		node.mindMapId = id;

		MindMap mindMap = new MindMap();
		mindMap.mindMapId = id;
		mindMap.name = tabMindMap.name;
		mindMap.addNode(node);

		updateRecentMindMap(id);

		return mindMap;
	}

	public MindMap getRecentMindMap() {
		MindMap mindMap = new MindMap();
		TabMindMap tmm = mindMapDao.getCurrentMindMap();
		if (tmm == null) {
			return null;
		}

		mindMap.mindMapId = tmm._id;
		mindMap.nodeList = nodeDao.getAllNodesBy(tmm._id);
		orderNodeList(mindMap.nodeList);
		return mindMap;
	}

	private void updateRecentMindMap(long mindMapId) {
		mindMapDao.updateRecentMindMap(mindMapId);
	}

	private void orderNodeList(List<Node> nodeList) {
		Hashtable<Long, Node> ht = new Hashtable<Long, Node>();
		for (Node node : nodeList) {
			if(!ht.containsKey(node._id)){
				ht.put(node._id, node);
			}
			
			if(ht.containsKey(node.parentNodeId)){
				Node parentNode = ht.get(node.parentNodeId);
				node.setParentNode(parentNode);
			}
		}
	}

	public MindMap getMindMapBy(long mindMapId) {
		MindMap mindMap = new MindMap();
		TabMindMap tmm = mindMapDao.getMindMapBy(mindMapId);
		if (tmm == null) {
			return null;
		}

		mindMap.mindMapId = tmm._id;
		mindMap.nodeList = nodeDao.getAllNodesBy(tmm._id);
		orderNodeList(mindMap.nodeList);
		
		updateRecentMindMap(mindMapId);
		return mindMap;
	}

}
