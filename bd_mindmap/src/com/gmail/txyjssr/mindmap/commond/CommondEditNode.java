package com.gmail.txyjssr.mindmap.commond;

import com.gmail.txyjssr.mindmap.EditTextNode;
import com.gmail.txyjssr.mindmap.MindMap;
import com.gmail.txyjssr.mindmap.MindMapActivity;
import com.gmail.txyjssr.mindmap.Node;
import com.gmail.txyjssr.mindmap.R;
import com.gmail.txyjssr.mindmap.R.id;

import android.widget.TextView;

public class CommondEditNode implements ICommond {
	private MindMapActivity mmActivity;
	private Node editNode;
	private MindMap mindMap;
	private String newTitle;
	private String oldTitle;
	
	public CommondEditNode(MindMapActivity mmActivity,MindMap mindMap, Node editNode,String newTitle,String oldTitle) {
		this.mmActivity = mmActivity;
		this.mindMap = mindMap;
		this.editNode = editNode;
		this.newTitle = newTitle;
		this.oldTitle = oldTitle;
	}

	@Override
	public void redo() {
		editNode.title = newTitle;
		mindMap.updateNodeTile(editNode);
		EditTextNode nl = (EditTextNode) mmActivity.findViewById((int) editNode._id);
		nl.setTitle(newTitle);
		TextView tvMindMapName = (TextView)mmActivity.findViewById(R.id.tv_mind_map_name);
		tvMindMapName.setText(mindMap.name);
	}

	@Override
	public void undo() {
		editNode.title = oldTitle;
		mindMap.updateNodeTile(editNode);
		EditTextNode nl = (EditTextNode) mmActivity.findViewById((int) editNode._id);
		nl.setTitle(oldTitle);
		TextView tvMindMapName = (TextView)mmActivity.findViewById(R.id.tv_mind_map_name);
		tvMindMapName.setText(mindMap.name);
	}

}
