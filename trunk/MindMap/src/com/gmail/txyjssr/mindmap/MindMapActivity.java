package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.txyjssr.mindmap.EditTextNode.OnMoveListener;
import com.gmail.txyjssr.mindmap.NodeLayout.OnButtonClickListener;

public class MindMapActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnButtonClickListener,
		OnMoveListener {
	private static final int REQUST_CODE_MANAGE_MINDMAP = 1;

	private MindMapView mindMapPad;
	private MindMapManager mindMapManager;
	private MindMap mindMap;
	private EditTextNode currentFocusedNode;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);

		ImageView ivMindMapMore = (ImageView) findViewById(R.id.iv_mind_map_more);
		ivMindMapMore.setOnClickListener(this);
		
		ImageView ivRoot = (ImageView)findViewById(R.id.iv_root);
		ImageView ivClear = (ImageView)findViewById(R.id.iv_clear);
		ImageView ivDelete = (ImageView)findViewById(R.id.iv_delete);
		ImageView ivAdd = (ImageView)findViewById(R.id.iv_add);
		ImageView ivEdit = (ImageView)findViewById(R.id.iv_edit);
		ivRoot.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		ivDelete.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		ivEdit.setOnClickListener(this);

		mindMapPad = (MindMapView) findViewById(R.id.fl_pad);
		mindMapPad.setOnClickListener(this);
		mindMapManager = new MindMapManager();

		mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap();
		}

		createMindMapUI(mindMap);
	}

	private void createMindMapUI(MindMap mindMap) {
		TextView tvName = (TextView)findViewById(R.id.tv_mind_map_name);
		tvName.setText(mindMap.name);
		mindMapPad.removeAllViews();
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
			case R.id.iv_mind_map_more:
				Intent intent = new Intent(this, MMManagerActivity.class);
				startActivityForResult(intent, REQUST_CODE_MANAGE_MINDMAP);
				break;
			case R.id.iv_root:
				moveToRootNode();
				break;
			case R.id.iv_clear:
				clearNodes();
				break;
			case R.id.iv_delete:
				deleteNode();
				break;
			case R.id.iv_add:
				addNode();
				break;
			case R.id.iv_edit:
				editNode();
				break;
			}
		}
	}

	private void editNode() {
		//show dialog
		
	}

	private void addNode() {
		if(currentFocusedNode!=null){
			addNode((Node)currentFocusedNode.getTag());
		}
	}

	private void deleteNode() {
		if(currentFocusedNode!=null){
			deleteNode((Node)currentFocusedNode.getTag());
		}
		
	}

	private void clearNodes() {
		Node rootNode = mindMap.getRootNode();
		deleteNode(rootNode);
		
	}

	private void moveToRootNode() {
		Node rootNode = mindMap.getRootNode();
		mindMapPad.moveToNodeLocation(rootNode.x,rootNode.y);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUST_CODE_MANAGE_MINDMAP) {
			if (resultCode == MMManagerActivity.RESULT_CODE_NEW) {
				mindMap = mindMapManager.createMindMap();
				createMindMapUI(mindMap);
			} else if (resultCode == MMManagerActivity.RESULT_CODE_OPEN) {
				long mindMapId = data.getLongExtra(MMManagerActivity.EXTRA_OPEN_MINDMAP_ID, -1);
				if (mindMapId != -1 && mindMapId != mindMap.mindMapId) {
					mindMap = mindMapManager.getMindMapBy(mindMapId);
					createMindMapUI(mindMap);
				}
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
				setButtonVisible((Node) etNode.getTag(), View.VISIBLE);
				Runnable r = new Runnable() {

					@Override
					public void run() {
						currentFocusedNode.setEditEnable(true);
					}
				};
				mHandler.postDelayed(r, 100);
			} else {
				setButtonVisible((Node) etNode.getTag(), View.INVISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etNode.getWindowToken(), 0);
				EditTextNode etn = (EditTextNode) v;
				String title = etn.getTitle();
				Node node = (Node) etn.getTag();
				if (TextUtils.isEmpty(title.trim())) {
					if (TextUtils.isEmpty(node.title)) {
						deleteNode(node);
					} else {
						etNode.setText(node.title);
					}
				} else if (!title.equals(node.title)) {
					node.title = title;
					updateNodeTitle(node);
				}
				etNode.setEditEnable(false);
				currentFocusedNode = null;
			}
		} else {
			currentFocusedNode = null;
		}

	}

	private void setButtonVisible(Node node, int visible) {
//		NodeLayout nl = (NodeLayout) findViewById((int) node._id);
//		nl.setButtonVisible(visible);
	}

	private void updateNodeTitle(Node node) {
		mindMap.updateNodeTile(node);

	}

	@Override
	public void onAddClick(Node node) {
		if (!TextUtils.isEmpty(node.title)) {
			addNode(node);
		}
	}

	@Override
	public void onDeleteClick(Node node) {
		deleteNode(node);
	}

	private void addNode(Node parentNode) {
		
		if(parentNode.nodeChildren.size()>=20){
			Toast.makeText(this, "the node already had 20 nodes", Toast.LENGTH_LONG).show();
			return;
		}
		
		Node childNode = new Node();
		childNode.title = "test";
		childNode.setParentNode(parentNode);
		mindMap.addNode(childNode);

		NodeLayout nv = createNodeLayout(childNode);
//		nv.setEditEnable(true);
		mindMapPad.addView(nv);
//		nv.requestEditFocus();

		int addIndex = mindMap.getNodes().size() - 2;
		LinkView lv = createLinkView(childNode);
		mindMapPad.addView(lv, addIndex);
	}

	private void deleteNode(Node node) {
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
		nv.setOnMoveListener(this);
		return nv;
	}

	private LinkView createLinkView(Node node) {
		LinkView lv = new LinkView(this);
		lv.setLink(node.parentNode.x, node.parentNode.y, node.x, node.y);
		lv.setTag(node._id);
		return lv;
	}

	@Override
	protected void onDestroy() {
		if (currentFocusedNode != null) {
			String title = currentFocusedNode.getTitle();
			Node node = (Node) currentFocusedNode.getTag();
			if (TextUtils.isEmpty(title.trim())) {
				if (TextUtils.isEmpty(node.title)) {
					deleteNode(node);
				}
			} else if (!title.equals(node.title)) {
				node.title = title;
				updateNodeTitle(node);
			}
		}
		super.onDestroy();
	}

	@Override
	public void onMove(EditTextNode etn) {
		Node node = (Node) etn.getTag();
		LinkView lv = (LinkView) mindMapPad.findViewWithTag(node._id);
		if (lv != null) {
			lv.childX = etn.getPointX();
			lv.childY = etn.getPointY();
		}
		List<Node> childrenNodes = node.nodeChildren;
		for(Node n:childrenNodes){
			LinkView tlv = (LinkView) mindMapPad.findViewWithTag(n._id);
			if (tlv != null) {
				tlv.parentX = etn.getPointX();
				tlv.parentY = etn.getPointY();
			}
		}
	}

	@Override
	public void endMove(EditTextNode etn) {
		Node node = (Node) etn.getTag();
		node.x = etn.getPointX();;
		node.y = etn.getPointY();
		mindMap.updateNodeLocation(node);
	}

}
