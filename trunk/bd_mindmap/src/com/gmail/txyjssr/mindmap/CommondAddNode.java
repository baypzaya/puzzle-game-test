package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.view.View;

public class CommondAddNode implements ICommond {
	private MindMapActivity mmActivity;
	private Node parentNode;
	private Node childNode;
	private String childTitle;
	private MindMap mindMap;
	private MindMapView mindMapPad;

	public CommondAddNode(MindMapActivity mmActivity, Node parentNode, String childTitle, MindMap mindMap, MindMapView mindMapPad,Node chilNode) {
		this.mmActivity = mmActivity;
		this.parentNode = parentNode;
		this.childTitle = childTitle;
		this.mindMap = mindMap;
		this.mindMapPad = mindMapPad;
		this.childNode = chilNode;
	}

	@Override
	public void redo() {
		childNode = new Node();
		childNode.title = childTitle;
		childNode.setParentNode(parentNode);
		mindMap.addNode(childNode);

		NodeLayout nv = createNodeLayout(childNode);
		mindMapPad.addView(nv);

		int addIndex = mindMap.getNodes().size() - 2;
		LinkView lv = createLinkView(childNode);
		mindMapPad.addView(lv, addIndex);

		nv.requestEditFocus();

	}

	@Override
	public void undo() {
		List<Node> nodeList = mindMap.removeNode(childNode);
		for (Node n : nodeList) {
			View nodeView = mindMapPad.findViewById((int) n._id);
			mindMapPad.removeView(nodeView);
			View linkView = mindMapPad.findViewWithTag(n._id);
			mindMapPad.removeView(linkView);
		}
	}

	private NodeLayout createNodeLayout(Node node) {
		NodeLayout nv = new NodeLayout(mmActivity);
		nv.setId((int) node._id);
		nv.setTitle(node.title);
		nv.setLocation(node.x, node.y);

		nv.setNode(node);
		nv.setOnFocusChangeListener(mmActivity);
		nv.setOnMoveListener(mmActivity);
		return nv;
	}

	private LinkView createLinkView(Node node) {
		LinkView lv = new LinkView(mmActivity);
		lv.setLink(node.parentNode.x, node.parentNode.y, node.x, node.y);
		lv.setTag(node._id);
		return lv;
	}

}
