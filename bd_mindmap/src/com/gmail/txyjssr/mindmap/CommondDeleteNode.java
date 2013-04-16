package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.view.View;

public class CommondDeleteNode implements ICommond {
	private MindMapActivity mmActivity;
	private Node deleteNode;
	private List<Node> deleteNodeList;
	private MindMap mindMap;
	private MindMapView mindMapPad;
	
	public CommondDeleteNode(MindMapActivity mmActivity,MindMap mindMap, MindMapView mindMapPad,Node deleteNode,List<Node> deleteNodeList) {
		this.mmActivity = mmActivity;
		this.mindMap = mindMap;
		this.mindMapPad = mindMapPad;
		this.deleteNode = deleteNode;
		this.deleteNodeList = deleteNodeList;
		mindMap.orderNodeList(this.deleteNodeList);
	}
	

	@Override
	public void redo() {
		List<Node> nodeList = mindMap.removeNode(deleteNode);
		for (Node n : nodeList) {
			View nodeView = mindMapPad.findViewById((int) n._id);
			mindMapPad.removeView(nodeView);
			View linkView = mindMapPad.findViewWithTag(n._id);
			mindMapPad.removeView(linkView);
		}
	}

	@Override
	public void undo() {
		
		for (Node childNode : deleteNodeList) {
			mindMap.addNode(childNode,true);
			EditTextNode nv = new EditTextNode(mmActivity);
			nv.setNode(childNode);
			mindMapPad.addView(nv);
		}
		
		for (Node childNode : deleteNodeList) {
			LinkView lv = new LinkView(mindMapPad,childNode);
			mindMapPad.addView(lv, 0);
		}
	}

}
