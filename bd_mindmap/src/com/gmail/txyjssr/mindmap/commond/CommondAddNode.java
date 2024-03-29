package com.gmail.txyjssr.mindmap.commond;

import java.util.List;

import android.view.View;

import com.gmail.txyjssr.mindmap.EditTextNode;
import com.gmail.txyjssr.mindmap.LinkView;
import com.gmail.txyjssr.mindmap.MindMap;
import com.gmail.txyjssr.mindmap.MindMapActivity;
import com.gmail.txyjssr.mindmap.MindMapView;
import com.gmail.txyjssr.mindmap.db.Node;

public class CommondAddNode implements ICommond {
	private MindMapActivity mmActivity;
	private Node childNode;
	private MindMap mindMap;
	private MindMapView mindMapPad;

	public CommondAddNode(MindMapActivity mmActivity,MindMap mindMap, MindMapView mindMapPad,Node chilNode) {
		this.mmActivity = mmActivity;
		this.mindMap = mindMap;
		this.mindMapPad = mindMapPad;
		this.childNode = chilNode;
	}

	@Override
	public void redo() {
		mindMap.addNode(childNode,true);
		EditTextNode nv = createNodeLayout(childNode);
		mindMapPad.addView(nv);
		int addIndex = mindMap.getNodes().size() - 2;
		LinkView lv = createLinkView(childNode);
		mindMapPad.addView(lv, addIndex);
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

	private EditTextNode createNodeLayout(Node node) {
		EditTextNode nv = new EditTextNode(mmActivity);
		nv.setId((int) node._id);
		nv.setTitle(node.title);
		nv.setLocation(node.x, node.y);

		nv.setNode(node);
		nv.setOnFocusChangeListener(mmActivity);
		nv.setOnMoveListener(mmActivity);
		return nv;
	}

	private LinkView createLinkView(Node node) {
		LinkView lv = new LinkView(mindMapPad,node);
		return lv;
	}

}
