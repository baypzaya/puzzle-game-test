package com.gmail.txyjssr.mindmap;

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
		node.y=0;
		node.title = "new mindmap";
		node.mindMapId = id;
		
		nodeDao.insert(node);
		
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
		if(tmm==null){
			return null;
		}
		
		
		return mindMap;
	}
	
	private void updateRecentMindMap(long mindMapId){
		mindMapDao.updateRecentMindMap(mindMapId);
	}

}
