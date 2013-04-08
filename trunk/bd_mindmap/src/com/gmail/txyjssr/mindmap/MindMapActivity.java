package com.gmail.txyjssr.mindmap;

import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.AdView;
import com.gmail.txyjssr.mindmap.EditTextNode.OnMoveListener;

public class MindMapActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnMoveListener {
	private static final int REQUST_CODE_MANAGE_MINDMAP = 1;

	private MindMapView mindMapPad;
	private MindMapManager mindMapManager;
	private MindMap mindMap;
	private EditTextNode currentFocusedNode;

	private AdView adView;

	private CommondStack commondStack = new CommondStack();

	private ImageView ivUndo;

	private ImageView ivRedo;

	private Point oldPoint;
	private boolean isPreBack = false;
	
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);

		adView = (AdView) findViewById(R.id.adView);
		adView.setListener(new MyAdViewListener(adView));

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		EngineApplication.sDensity = dm.scaledDensity;

		ImageView ivMindMapMore = (ImageView) findViewById(R.id.iv_mind_map_more);
		ivMindMapMore.setOnClickListener(this);

		ImageView ivRoot = (ImageView) findViewById(R.id.iv_root);
		ImageView ivClear = (ImageView) findViewById(R.id.iv_clear);
		ImageView ivDelete = (ImageView) findViewById(R.id.iv_delete);
		ImageView ivAdd = (ImageView) findViewById(R.id.iv_add);
		ImageView ivEdit = (ImageView) findViewById(R.id.iv_edit);
		ivUndo = (ImageView) findViewById(R.id.iv_undo);
		ivRedo = (ImageView) findViewById(R.id.iv_redo);
		ivRoot.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		ivDelete.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		ivEdit.setOnClickListener(this);
		ivUndo.setOnClickListener(this);
		ivRedo.setOnClickListener(this);

		mindMapPad = (MindMapView) findViewById(R.id.fl_pad);
		mindMapPad.setOnClickListener(this);
		mindMapManager = new MindMapManager();

		mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap(getString(R.string.new_mind_map));
		}

		createMindMapUI(mindMap);

		updateRedoAndUndoState();
	}

	private void createMindMapUI(MindMap mindMap) {
		TextView tvName = (TextView) findViewById(R.id.tv_mind_map_name);
		tvName.setText(mindMap.name);
		mindMapPad.removeAllViews();
		List<Node> nodeList = mindMap.getNodes();
		for (Node node : nodeList) {
			EditTextNode nl = createNodeLayout(node);
			mindMapPad.addView(nl);
			if (!node.isRootNode) {
				LinkView lv = createLinkView(node);
				mindMapPad.addView(lv, 0);
			}else{
				mindMapPad.requestMoveToNode(nl);
			}
		}
		Node rootNode = mindMap.getRootNode();
		mindMapPad.scrollTo((int) rootNode.x, (int) rootNode.y);

		if (nodeList.size() == 1) {
			EditTextNode nl = (EditTextNode) mindMapPad.findViewById((int) rootNode._id);
			nl.requestFocus();
		}
		
		commondStack.clear();
		updateRedoAndUndoState();
	}

	@Override
	public void onClick(View v) {
		if (v instanceof MindMapView) {
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
			case R.id.iv_undo:
				undo();
				break;
			case R.id.iv_redo:
				redo();
				break;
			}
		}
	}

	private void redo() {
		commondStack.redo();
		updateRedoAndUndoState();
	}

	private void undo() {
		commondStack.undo();
		updateRedoAndUndoState();
	}

	private void editNode() {
		if (currentFocusedNode != null) {
			DialogUtils.showInputDialog(this, getString(R.string.edit_node), currentFocusedNode.getTitle(),
					new InputListener() {

						@Override
						public void onInputCompleted(String inputStr) {
							if (!TextUtils.isEmpty(inputStr)) {
								editNode((Node) currentFocusedNode.getTag(), inputStr);
							}
						}
					});

		}

	}

	private void addNode() {
		if (currentFocusedNode != null) {
			DialogUtils.showInputDialog(this, getString(R.string.add_node), null, new InputListener() {

				@Override
				public void onInputCompleted(String inputStr) {
					if (!TextUtils.isEmpty(inputStr)) {
						addNode((Node) currentFocusedNode.getTag(), inputStr);
					}
				}
			});

		}
	}

	private void deleteNode() {
		if (currentFocusedNode != null) {
			final Node node = (Node) currentFocusedNode.getTag();
			String message = getString(R.string.delete_node);// "delete node(" +
																// node.title +
																// ")";
			Formatter ft = new Formatter().format(message, node.title);

			DialogUtils.showHintDilog(this, ft.toString(), getString(R.string.delete), getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_POSITIVE) {
								deleteNode(node);
							}

						}

					});

		}

	}

	private void clearNodes() {
		final Node rootNode = mindMap.getRootNode();
		String message = getString(R.string.delete_all_nodes);
		DialogUtils.showHintDilog(this, message, getString(R.string.delete_all), getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_POSITIVE) {
							deleteNode(rootNode);
						}
					}

				});

	}

	private void moveToRootNode() {
		Node rootNode = mindMap.getRootNode();
		mindMapPad.moveToNodeLocationTocenter((EditTextNode)findViewById((int)rootNode._id));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUST_CODE_MANAGE_MINDMAP) {
			if (resultCode == MMManagerActivity.RESULT_CODE_NEW) {
				String mindMapName = data.getStringExtra(MMManagerActivity.EXTRA_NEW_MINDMAP_NAME);
				mindMap = mindMapManager.createMindMap(mindMapName);
				createMindMapUI(mindMap);
			} else if (resultCode == MMManagerActivity.RESULT_CODE_OPEN) {
				long mindMapId = data.getLongExtra(MMManagerActivity.EXTRA_OPEN_MINDMAP_ID, -1);
				if (mindMapId != -1 && mindMapId != mindMap.mindMapId) {
					mindMap = mindMapManager.getMindMapBy(mindMapId);
					createMindMapUI(mindMap);
				}
			} else {
				boolean isExist = mindMapManager.isExist(mindMap);
				if (!isExist) {
					mindMap = mindMapManager.createMindMap(getString(R.string.new_mind_map));
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
				currentFocusedNode = etNode;
				mindMapPad.scroll(currentFocusedNode);
				mindMapPad.bringChildToFront(etNode);
			} else {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etNode.getWindowToken(), 0);
				currentFocusedNode = null;
			}
		} else {
			currentFocusedNode = null;
		}

		if (currentFocusedNode != null) {
			findViewById(R.id.bottom_bar).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.bottom_bar).setVisibility(View.GONE);
		}

	}

	private void addNode(Node parentNode, String nodeTitle) {
		
		Node childNode = new Node();
		childNode.title = nodeTitle;
		childNode.setParentNode(parentNode);
		mindMap.computeLocation(childNode);
		mindMap.addNode(childNode);

		EditTextNode nv = createNodeLayout(childNode);
		mindMapPad.addView(nv);

		int addIndex = mindMap.getNodes().size() - 2;
		LinkView lv = createLinkView(childNode);
		mindMapPad.addView(lv, addIndex);
		nv.requestFocus();

		ICommond commond = new CommondAddNode(this, mindMap, mindMapPad, childNode);
		commondStack.pushCommond(commond);
		updateRedoAndUndoState();
	}

	private void updateRedoAndUndoState() {
		boolean redoEnable = commondStack.canRedo();
		boolean undoEnable = commondStack.canUndo();

		ivUndo.setEnabled(undoEnable);
		ivRedo.setEnabled(redoEnable);

	}

	private void editNode(Node node, String nodeTile) {
		String oldTitle = node.title;
		if (!TextUtils.isEmpty(nodeTile.trim()) && !oldTitle.equals(nodeTile)) {
			node.title = nodeTile;
			mindMap.updateNodeTile(node);
			EditTextNode nl = (EditTextNode) findViewById((int) node._id);
			nl.setTitle(nodeTile);
			TextView tvMindMapName = (TextView) findViewById(R.id.tv_mind_map_name);
			tvMindMapName.setText(mindMap.name);

			ICommond commond = new CommondEditNode(this, mindMap, node, nodeTile, oldTitle);
			commondStack.pushCommond(commond);
			updateRedoAndUndoState();
		}
	}

	private void deleteNode(Node node) {
		List<Node> nodeList = mindMap.removeNode(node);
		for (Node n : nodeList) {
			View nodeView = mindMapPad.findViewById((int) n._id);
			mindMapPad.removeView(nodeView);
			View linkView = mindMapPad.findViewWithTag(n._id);
			mindMapPad.removeView(linkView);
		}

		ICommond commond = new CommondDeleteNode(this, mindMap, mindMapPad, node, nodeList);
		commondStack.pushCommond(commond);
		updateRedoAndUndoState();
	}

	private EditTextNode createNodeLayout(Node node) {
		EditTextNode nv = new EditTextNode(this);
		nv.setNode(node);
		return nv;
	}

	private LinkView createLinkView(Node node) {
		LinkView lv = new LinkView(this, node);
		return lv;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void startMove(EditTextNode editTextNode) {
		oldPoint = new Point();
		oldPoint.x = editTextNode.getPointX();
		oldPoint.y = editTextNode.getPointY();
		mindMapPad.bringChildToFront(editTextNode);
	}

	@Override
	public void onMove(EditTextNode etn) {
		mindMapPad.scroll(etn);
	}

	@Override
	public void endMove(EditTextNode etn) {
		Node node = (Node) etn.getTag();
		node.x = etn.getPointX();
		node.y = etn.getPointY();
		mindMap.updateNodeLocation(node);
		
		Point newPoint = new Point();
		newPoint.x = node.x;
		newPoint.y = node.y;
		
		ICommond commont = new CommondMoveNode(this, mindMap, mindMapPad, node, newPoint,oldPoint);
		commondStack.pushCommond(commont);
		updateRedoAndUndoState();
		
		oldPoint = null;
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && !isPreBack){
			Toast.makeText(this, R.string.pre_back, Toast.LENGTH_LONG).show();
			isPreBack = true;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		isPreBack = false;
		return super.dispatchTouchEvent(ev);
	}
	
	

}
