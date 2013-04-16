package com.gmail.txyjssr.mindmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
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
	private EditTextNode currentMergeNode;
	private View focusImageView;
	private AdView adView;

	private CommondStack commondStack = new CommondStack();

	private ImageView ivUndo;

	private ImageView ivRedo;

	private Point oldPoint;
	private boolean isPreBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);
		// baidu code start
		// StatService.setOn(this,StatService.EXCEPTION_LOG);
		adView = (AdView) findViewById(R.id.adView);
		adView.setListener(new MyAdViewListener(adView));
		// baidu code end

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
		ImageView ivSend = (ImageView) findViewById(R.id.iv_send);
		ivUndo = (ImageView) findViewById(R.id.iv_undo);
		ivRedo = (ImageView) findViewById(R.id.iv_redo);
		ivRoot.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		ivDelete.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		ivEdit.setOnClickListener(this);
		ivSend.setOnClickListener(this);
		ivUndo.setOnClickListener(this);
		ivRedo.setOnClickListener(this);

		mindMapPad = (MindMapView) findViewById(R.id.fl_pad);
		mindMapPad.setOnClickListener(this);
		mindMapManager = new MindMapManager();

		focusImageView = new View(this);
		focusImageView.setBackgroundResource(R.drawable.focuse_bg);
		focusImageView.setVisibility(View.GONE);

		mindMap = mindMapManager.getRecentMindMap();
		if (mindMap == null) {
			mindMap = mindMapManager.createMindMap(getString(R.string.new_mind_map));
		}

		createMindMapUI(mindMap);
		updateRedoAndUndoState();
	}

	// public void onResume() {
	// super.onResume();
	//
	// //baidu code start
	// StatService.onResume(this);
	// //baidu code end
	// }
	//
	// public void onPause() {
	// super.onPause();
	// //baidu code start
	// StatService.onPause(this);
	// //baidu code end
	// }

	private void createMindMapUI(MindMap mindMap) {
		TextView tvName = (TextView) findViewById(R.id.tv_mind_map_name);
		tvName.setText(mindMap.name);
		mindMapPad.removeAllViews();
		mindMapPad.addView(focusImageView);
		List<Node> nodeList = mindMap.getNodes();
		for (Node node : nodeList) {
			EditTextNode nl = createNodeLayout(node);
			mindMapPad.addView(nl);
			if (node.isRootNode) {
				mindMapPad.requestMoveToNode(nl);
			}
		}
		
		for (Node node : nodeList) {
			if (!node.isRootNode) {
				LinkView lv = createLinkView(node);
				mindMapPad.addView(lv, 0);
			}
		}
		
		Node rootNode = mindMap.getRootNode();

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
			case R.id.iv_send:
				sendMindMap();
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

	private void sendMindMap() {
		
		String path = BitmapUtils.createMindMap(this, mindMap.mindMapId);
		File file = new File(path);
		
		Uri uri = Uri.fromFile(file);
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(uri,"image/jpeg");
		startActivity(intent);
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
							} else {
								Toast.makeText(MindMapActivity.this, R.string.hint_node_name_empty, Toast.LENGTH_SHORT)
										.show();
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
					} else {
						Toast.makeText(MindMapActivity.this, R.string.hint_node_name_empty, Toast.LENGTH_SHORT).show();
					}
				}
			});

		}
	}

	private void deleteNode() {
		if (currentFocusedNode != null) {
			final Node node = (Node) currentFocusedNode.getTag();
			String message = getString(R.string.delete_node);
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
		mindMapPad.requestMoveToNode((EditTextNode) findViewById((int) rootNode._id));
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
				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE);
				// imm.hideSoftInputFromWindow(etNode.getWindowToken(), 0);
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
		LinkView lv = new LinkView(mindMapPad, node);
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
		int x = (int) (etn.getPointX() + etn.getMeasuredWidth() / 2);
		int y = (int) (etn.getPointY() + etn.getMeasuredHeight() / 2);
		Node node = (Node) etn.getTag();
		boolean isMerge = false;
		if (!node.isRootNode) {
			int childCount = mindMapPad.getChildCount();
			for (int i = childCount - 1; i > 0; i--) {
				View child = mindMapPad.getChildAt(i);
				if (child instanceof EditTextNode) {
					EditTextNode etnC = (EditTextNode) child;
					if (node._id == etnC.getId() || node.parentNodeId == etnC.getId()) {
						continue;
					}

					if (etnC.containPoint(x, y)) {
						focusImageView.setVisibility(View.VISIBLE);
						isMerge = true;
						if (currentMergeNode == null || currentMergeNode.getId() != etnC.getId()) {
							currentMergeNode = etnC;
							int widthC = etnC.getMeasuredWidth();
							int heightC = etnC.getMeasuredHeight();
							int xC = (int) (etnC.getPointX() + widthC / 2);
							int yC = (int) (etnC.getPointY() + heightC / 2);
							int margin = (int)EngineApplication.transformDP2PX(17);
							int widthF = widthC + margin;
							int heightF = heightC + margin;
							int xF = (int) xC - widthF / 2;
							int yF = (int) yC - heightF / 2;
							LayoutParams p = new AbsoluteLayout.LayoutParams(widthF, heightF, xF, yF);
							focusImageView.setLayoutParams(p);
							focusImageView.requestLayout();
							break;
						}
					}
				}
			}
		}

		if (!isMerge) {
			focusImageView.setVisibility(View.GONE);
			currentMergeNode = null;
		}

	}

	@Override
	public void endMove(EditTextNode etn) {
		if (currentMergeNode == null) {
			Node node = (Node) etn.getTag();
			node.x = etn.getPointX();
			node.y = etn.getPointY();
			mindMap.updateNodeLocation(node);

			Point newPoint = new Point();
			newPoint.x = node.x;
			newPoint.y = node.y;

			ICommond commont = new CommondMoveNode(this, mindMap, mindMapPad, node, newPoint, oldPoint);
			commondStack.pushCommond(commont);
			updateRedoAndUndoState();
			oldPoint = null;
		} else {
			Node newParentNode = (Node) currentMergeNode.getTag();
			Node childNode = (Node) etn.getTag();

			Node oldParentNode = childNode.parentNode;
			oldParentNode.nodeChildren.remove(childNode);
			childNode.setParentNode(newParentNode);

			Point oldPoint = new Point();
			oldPoint.x = childNode.x;
			oldPoint.y = childNode.y;

			mindMap.computeLocation(childNode);
			mindMap.updateNodeLocation(childNode);

			Point newPoint = new Point();
			newPoint.x = childNode.x;
			newPoint.y = childNode.y;

			etn.setNode(childNode);

			LinkView lv = (LinkView) mindMapPad.findViewWithTag(childNode._id);
			lv.parentEtn = currentMergeNode;
			
			mindMapPad.scroll(etn);

			focusImageView.setVisibility(View.GONE);
			currentMergeNode = null;

			ICommond commont = new CommondMergeNode(mindMap, mindMapPad, oldParentNode, newParentNode, childNode,
					oldPoint, newPoint);
			commondStack.pushCommond(commont);
			updateRedoAndUndoState();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !isPreBack) {
			Toast.makeText(this, R.string.pre_back, Toast.LENGTH_SHORT).show();
			isPreBack = true;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isPreBack) {
			isPreBack = false;
		}
		return super.dispatchTouchEvent(ev);
	}

}
