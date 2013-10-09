package com.gmail.txyjssr.mindmap.commond;

import java.util.List;

import com.gmail.txyjssr.mindmap.EditTextNode;
import com.gmail.txyjssr.mindmap.LinkView;
import com.gmail.txyjssr.mindmap.MindMap;
import com.gmail.txyjssr.mindmap.MindMapActivity;
import com.gmail.txyjssr.mindmap.MindMapView;
import com.gmail.txyjssr.mindmap.db.Node;

import android.view.View;

public class CommondDeleteNode implements ICommond {
	private MindMapActivity mmActivity;
	private Node deleteNode;
	private List<Node> deleteNodeList;
	private MindMap mindMap;
	private MindMapView mindMapPad;

	public CommondDeleteNode(MindMapActivity mmActivity, MindMap mindMap, MindMapView mindMapPad, Node deleteNode,
			List<Node> deleteNodeList) {
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
			mindMap.addNode(childNode, true);
			EditTextNode nv = new EditTextNode(mmActivity);
			nv.setNode(childNode);
			mindMapPad.addView(nv);
			
			if(childNode.parentNode == null){
				EditTextNode pnv = (EditTextNode) mindMapPad.findViewById((int)childNode.parentNodeId);
				childNode.parentNode = (Node)pnv.getTag();
			}
		}

		for (Node childNode : deleteNodeList) {
			if (!childNode.isRootNode) {
				LinkView lv = new LinkView(mindMapPad, childNode);
				mindMapPad.addView(lv, 0);
			}
		}
	}

}
