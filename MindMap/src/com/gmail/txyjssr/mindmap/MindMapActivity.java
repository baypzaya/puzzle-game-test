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
import android.widget.FrameLayout;

public class MindMapActivity extends Activity implements OnClickListener, OnTouchListener, OnFocusChangeListener {
	private FrameLayout mindMapPad;
	private MindMapManager mindMapManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);

		mindMapPad = (FrameLayout) findViewById(R.id.fl_pad);
		mindMapManager = new MindMapManager();
		
//		int mw = mindMapPad.getMeasuredWidth();
//		Log.i("yujsh log","mw:"+mw);

		MindMap mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap();
			Node rootNode = new Node();
			rootNode.isRootNode = true;
			rootNode.title = "";
			mindMap.addNode(rootNode);
		}

		createMindMapUI(mindMap);
	}

	private void createMindMapUI(MindMap mindMap) {
		List<Node> nodeList = mindMap.getNodes();
		for (Node node : nodeList) {
			NodeView et = new NodeView(this,node);
			mindMapPad.addView(et);
		}
	}

	@Override
	public void onClick(View v) {
		Log.i("yujsh log", "v:" + v);
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
