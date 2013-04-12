package com.gmail.txyjssr.mindmap;

public class CommondMergeNode implements ICommond {

	private Node oldParentNode;
	private Node newParentNode;
	private Node mergeNode;
	private MindMap mindMap;
	private MindMapView mindMapPad;
	private Point oldMergeNodePoint;
	private Point newMergeNodePoint;

	public CommondMergeNode(MindMap mindMap, MindMapView mindMapPad, Node oldParentNode, Node newParentNode,
			Node mergeNode, Point oldMergeNodePoint,Point newMergeNodePoint) {
		this.mindMap = mindMap;
		this.mindMapPad = mindMapPad;
		this.oldParentNode = oldParentNode;
		this.newParentNode = newParentNode;
		this.mergeNode = mergeNode;
		this.oldMergeNodePoint = oldMergeNodePoint;
		this.newMergeNodePoint = newMergeNodePoint;
	}

	@Override
	public void redo() {

		oldParentNode.nodeChildren.remove(mergeNode);
		mergeNode.setParentNode(newParentNode);

		mergeNode.x = newMergeNodePoint.x;
		mergeNode.y = newMergeNodePoint.y;
		mindMap.updateNodeLocation(mergeNode);

		EditTextNode etn = (EditTextNode) mindMapPad.findViewById((int) mergeNode._id);
		etn.setLocation(mergeNode.x, mergeNode.y);

		LinkView lv = (LinkView) mindMapPad.findViewWithTag(mergeNode._id);
		lv.parentEtn = (EditTextNode) mindMapPad.findViewById((int) newParentNode._id);

		mindMapPad.scroll(etn);

	}

	@Override
	public void undo() {
		newParentNode.nodeChildren.remove(mergeNode);
		mergeNode.setParentNode(oldParentNode);

		mergeNode.x = oldMergeNodePoint.x;
		mergeNode.y = oldMergeNodePoint.y;
		mindMap.updateNodeLocation(mergeNode);

		EditTextNode etn = (EditTextNode) mindMapPad.findViewById((int) mergeNode._id);
		etn.setLocation(mergeNode.x, mergeNode.y);

		LinkView lv = (LinkView) mindMapPad.findViewWithTag(mergeNode._id);
		lv.parentEtn = (EditTextNode) mindMapPad.findViewById((int) oldParentNode._id);

		mindMapPad.scroll(etn);

	}

}
