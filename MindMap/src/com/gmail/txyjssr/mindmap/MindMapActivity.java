package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class MindMapActivity extends Activity implements OnClickListener {
	private FrameLayout mindMapPad;
	private MindMapManager mindMapManager;
	private MindMap mindMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);

		mindMapPad = (FrameLayout) findViewById(R.id.fl_pad);
		mindMapManager = new MindMapManager();

		mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap();
			Node rootNode = new Node();
			rootNode.isRootNode = true;
			rootNode.title = "root node";
			mindMap.addNode(rootNode);
		}

		createMindMapUI(mindMap);
	}

	private void createMindMapUI(MindMap mindMap) {
		List<Node> nodeList = mindMap.getNodes();
		for (Node node : nodeList) {
			EditTextNode et = new EditTextNode(this,node);
			mindMapPad.addView(et);
			
			et.setTag(node);
			et.setOnClickListener(this);
			
		}
	}

	@Override
	public void onClick(View v) {
		if(v instanceof EditTextNode){
			Node node = (Node)v.getTag();
			Node childNode = new Node();
			childNode.setParentNode(node);
			childNode.title = "child node";
			
			mindMap.addNode(childNode);
			EditTextNode nv = new EditTextNode(this,childNode);
			mindMapPad.addView(nv);
			
			nv.setTag(childNode);
			nv.setOnClickListener(this);
			
			LinkView lv = new LinkView(this);
			lv.setLink(node.x,node.y,childNode.x,childNode.y);
			mindMapPad.addView(lv,0);
		}
	}

}
