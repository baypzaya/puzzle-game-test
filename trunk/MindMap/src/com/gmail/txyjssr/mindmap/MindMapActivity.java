package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.gmail.txyjssr.mindmap.NodeLayout.OnButtonClickListener;

public class MindMapActivity extends Activity implements OnClickListener, OnFocusChangeListener ,OnButtonClickListener{
	private FrameLayout mindMapPad;
	private MindMapManager mindMapManager;
	private MindMap mindMap;
	private EditTextNode currentFocusedNode;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);

		mindMapPad = (FrameLayout) findViewById(R.id.fl_pad);
		mindMapPad.setOnClickListener(this);
		mindMapManager = new MindMapManager();

		mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap();
			Node rootNode = new Node();
			rootNode.isRootNode = true;
			rootNode.title = "root node";
			mindMap.addNode(rootNode);

//			Node node = new Node();
//			node.isRootNode = false;
//			node.title = "node";
//			node.x = 200;
//			node.y = 200;
//			mindMap.addNode(node);
		}

		createMindMapUI(mindMap);
	}

	private void createMindMapUI(MindMap mindMap) {
		List<Node> nodeList = mindMap.getNodes();
		for (Node node : nodeList) {
//			EditTextNode et = new EditTextNode(this, node);
			NodeLayout nl = new NodeLayout(this);
			nl.setTitle(node.title);
			nl.setLocation(node.x, node.y);
			mindMapPad.addView(nl);
//			nl.setOnFocusChangeListener(this);
			nl.setEditEnable(false);
			nl.setNode(node);
			nl.setOnButtonListener(this);
//			nl.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof NodeLayout) {
			// EditTextNode etn = (EditTextNode) v;
			// if (etn.isFocused()) {
			// etn.setEditEnable(true);
			// }
//			v.setFocusableInTouchMode(true);
		} else {
			if (currentFocusedNode != null) {
				currentFocusedNode.setEditEnable(false);
			}
			findViewById(R.id.et_focus).requestFocus();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v instanceof EditTextNode) {
			EditTextNode etNode = (EditTextNode) v;
			if (etNode.isFocused()) {

				if (currentFocusedNode != null)
					currentFocusedNode.setEditEnable(false);
				currentFocusedNode = etNode;
				
				Runnable r = new Runnable() {

					@Override
					public void run() {
						currentFocusedNode.setEditEnable(true);
					}

				};
				mHandler.postDelayed(r, 100);
			}else{
				InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etNode.getWindowToken(), 0);
			}
		}

	}

	@Override
	public void onAddClick(Node node) {
		Log.i("yujsh log","onAddClick");
		 Node childNode = new Node();
		 childNode.setParentNode(node);
		 childNode.title = "child node";
		
		 mindMap.addNode(childNode);
		 NodeLayout nv = new NodeLayout(this);
		 nv.setTitle(childNode.title);
		 nv.setLocation(childNode.x, childNode.y);
		 mindMapPad.addView(nv);
		
		 nv.setNode(childNode);
//		 nv.setOnClickListener(this);
		 nv.setEditEnable(false);
		 nv.setOnButtonListener(this);
		
	}

	@Override
	public void onDeleteClick(Node node) {
		// TODO Auto-generated method stub
		
	}

}
