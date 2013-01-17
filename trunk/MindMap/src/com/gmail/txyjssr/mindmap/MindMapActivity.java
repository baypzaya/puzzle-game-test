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

public class MindMapActivity extends Activity implements OnClickListener, OnTouchListener, OnFocusChangeListener {
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
			NodeView et = new NodeView(this,node);
			mindMapPad.addView(et);
			
			et.setTag(node);
			et.setOnClickListener(this);
			
		}
	}

	@Override
	public void onClick(View v) {
		if(v instanceof NodeView){
			Node node = (Node)v.getTag();
			Node childNode = new Node();
			childNode.parentNode= node;
			mindMap.addNode(childNode);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			NodeView nv = new NodeView(this,childNode);
//			nv.setLayoutParams(lp);
			mindMapPad.addView(nv,lp);
			
			nv.setTag(childNode);
			nv.setOnClickListener(this);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Log.i("yujsh log", "v:" + v.getClass().getName());
	}

}
