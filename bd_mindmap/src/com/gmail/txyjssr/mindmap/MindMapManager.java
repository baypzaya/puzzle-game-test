package com.gmail.txyjssr.mindmap;

import java.util.Hashtable;
import java.util.List;

public class MindMapManager {
	MindMapDao mindMapDao = new MindMapDao();
	NodeDao nodeDao = new NodeDao();
	
	public MindMap createMindMap(String name) {
		TabMindMap tabMindMap = new TabMindMap();
		tabMindMap.name = name;
		tabMindMap.isCurrent = true;
		long id = mindMapDao.insert(tabMindMap);

		Node node = new Node();
		node.isRootNode = true;
		node.parentNodeId = -1;
		node.x = 0;
		node.y = 0;
		node.title = name;
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
		mindMap.name = tmm.name;
		mindMap.setNodeList(nodeDao.getAllNodesBy(tmm._id));
		return mindMap;
	}

	private void updateRecentMindMap(long mindMapId) {
		mindMapDao.updateRecentMindMap(mindMapId);
	}

	public MindMap getMindMapBy(long mindMapId) {
		MindMap mindMap = new MindMap();
		TabMindMap tmm = mindMapDao.getMindMapBy(mindMapId);
		if (tmm == null) {
			return null;
		}

		mindMap.mindMapId = tmm._id;
		mindMap.setNodeList(nodeDao.getAllNodesBy(tmm._id));
		mindMap.name = tmm.name;
		
		updateRecentMindMap(mindMapId);
		return mindMap;
	}

	public boolean isExist(MindMap mindMap) {
		TabMindMap tmm = mindMapDao.getMindMapBy(mindMap.mindMapId);
		return tmm!=null;
	}

}
