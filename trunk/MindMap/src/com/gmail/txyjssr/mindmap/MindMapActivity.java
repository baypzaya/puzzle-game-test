package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gmail.txyjssr.mindmap.NodeLayout.OnButtonClickListener;

public class MindMapActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnButtonClickListener {
	private FrameLayout mindMapPad;
	private MindMapManager mindMapManager;
	private MindMap mindMap;
	private EditTextNode currentFocusedNode;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);

		Button btnMindMap = (Button) findViewById(R.id.btn_mind_map);
		btnMindMap.setOnClickListener(this);

		mindMapPad = (FrameLayout) findViewById(R.id.fl_pad);
		mindMapPad.setOnClickListener(this);
		mindMapManager = new MindMapManager();

		mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap();
		}

		createMindMapUI(mindMap);
	}

	private void createMindMapUI(MindMap mindMap) {
		List<Node> nodeList = mindMap.getNodes();
		for (Node node : nodeList) {
			NodeLayout nl = createNodeLayout(node);
			mindMapPad.addView(nl);
			if (!node.isRootNode) {
				LinkView lv = createLinkView(node);
				mindMapPad.addView(lv, 0);
			}

		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof MindMapView) {
			if (currentFocusedNode != null) {
				currentFocusedNode.setEditEnable(false);
			}
			findViewById(R.id.et_focus).requestFocus();
		} else {
			int id = v.getId();
			switch (id) {
			case R.id.btn_mind_map:
				Intent intent  = new Intent(this,MMManagerActivity.class);
				startActivity(intent);
				break;
			}
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
			} else {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etNode.getWindowToken(), 0);
			}
		}

	}

	@Override
	public void onAddClick(Node node) {
		Node childNode = new Node();
		childNode.setParentNode(node);
		childNode.title = "child node";
		mindMap.addNode(childNode);

		NodeLayout nv = createNodeLayout(childNode);
		mindMapPad.addView(nv);

		LinkView lv = createLinkView(childNode);
		mindMapPad.addView(lv, 0);

	}

	@Override
	public void onDeleteClick(Node node) {
		List<Node> nodeList = mindMap.removeNode(node);
		for (Node n : nodeList) {
			View nodeView = mindMapPad.findViewById((int) n._id);
			mindMapPad.removeView(nodeView);
			View linkView = mindMapPad.findViewWithTag(n._id);
			mindMapPad.removeView(linkView);
		}

	}

	private NodeLayout createNodeLayout(Node node) {
		NodeLayout nv = new NodeLayout(this);
		nv.setId((int) node._id);
		nv.setTitle(node.title);
		nv.setLocation(node.x, node.y);

		nv.setNode(node);
		nv.setEditEnable(false);
		nv.setOnButtonListener(this);
		nv.setOnFocusChangeListener(this);
		return nv;
	}

	private LinkView createLinkView(Node node) {
		LinkView lv = new LinkView(this);
		lv.setLink(node.parentNode.x, node.parentNode.y, node.x, node.y);
		lv.setTag(node._id);
		return lv;
	}

}
