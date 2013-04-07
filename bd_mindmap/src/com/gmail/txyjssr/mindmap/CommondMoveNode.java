package com.gmail.txyjssr.mindmap;

import java.util.List;

public class CommondMoveNode implements ICommond {
	private MindMapActivity mmActivity;
	private Node moveNode;
	private MindMap mindMap;
	private MindMapView mindMapPad;
	private Point newPoint;
	private Point oldPoint;

	public CommondMoveNode(MindMapActivity mmActivity, MindMap mindMap, MindMapView mindMapPad, Node moveNode,
			Point newPoint, Point oldPoint) {
		this.mmActivity = mmActivity;
		this.mindMap = mindMap;
		this.mindMapPad = mindMapPad;
		this.moveNode = moveNode;
		this.newPoint = newPoint;
		this.oldPoint = oldPoint;
	}

	@Override
	public void redo() {
		updateNodeLocation(newPoint);
	}

	@Override
	public void undo() {
		updateNodeLocation(oldPoint);
	}

	private void updateNodeLocation(Point point) {
		LinkView lv = (LinkView) mindMapPad.findViewWithTag(moveNode._id);
		if (lv != null) {
			lv.childX = point.x;
			lv.childY = point.y;
		}
		List<Node> childrenNodes = moveNode.nodeChildren;
		for (Node n : childrenNodes) {
			LinkView tlv = (LinkView) mindMapPad.findViewWithTag(n._id);
			if (tlv != null) {
				tlv.parentX = point.x;
				tlv.parentY = point.y;
			}
		}

		NodeLayout nl = (NodeLayout) mmActivity.findViewById((int) moveNode._id);
		nl.setLocation(point.x, point.y);
		mindMapPad.scroll(nl.getEditNode());
		
		mindMapPad.requestLayout();

		moveNode.x = point.x;
		moveNode.y = point.y;
		mindMap.updateNodeLocation(moveNode);
	}

}
