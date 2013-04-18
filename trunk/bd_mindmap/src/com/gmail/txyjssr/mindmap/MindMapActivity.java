package com.gmail.txyjssr.mindmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.AdView;
import com.baidu.mobstat.StatService;
import com.gmail.txyjssr.mindmap.EditTextNode.OnMoveListener;

public class MindMapActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnMoveListener {
	private static final int REQUST_CODE_MANAGE_MINDMAP = 1;
	private static final int MSG_CREATE_IMAGE = 1;

	private MindMapView mindMapPad;
	private MindMapManager mindMapManager;
	private MindMap mindMap;
	private EditTextNode currentFocusedNode;
	private EditTextNode currentMergeNode;
	private View focusImageView;
	private AdView adView;
	private ImageView ivUndo;
	private ImageView ivRedo;
	private DropDownList dropDownList;

	private CommondStack commondStack = new CommondStack();
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case MSG_CREATE_IMAGE:
				String path = (String) msg.obj;
				processCreatedImage(path);
				break;
			}
		}
	};

	private Point oldPoint;
	private boolean isPreBack = false;
	private boolean isCancelCreateImage = false;

	private List<Node> notMoveToParentNodes = new ArrayList<Node>();
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_activity);
		// baidu code start
		// StatService.setOn(this,StatService.EXCEPTION_LOG);
		adView = (AdView) findViewById(R.id.adView);
		adView.setListener(new MyAdViewListener(adView));
		// baidu code endo

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		EngineApplication.sDensity = dm.scaledDensity;

		ImageView ivMindMapMore = (ImageView) findViewById(R.id.iv_mind_map_more);
		ivMindMapMore.setOnClickListener(this);

		ImageView ivDelete = (ImageView) findViewById(R.id.iv_delete);
		ImageView ivAdd = (ImageView) findViewById(R.id.iv_add);
		ImageView ivEdit = (ImageView) findViewById(R.id.iv_edit);
		ivUndo = (ImageView) findViewById(R.id.iv_undo);
		ivRedo = (ImageView) findViewById(R.id.iv_redo);
		ivDelete.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		ivEdit.setOnClickListener(this);
		ivUndo.setOnClickListener(this);
		ivRedo.setOnClickListener(this);

		dropDownList = new DropDownList(this);
		MyArrayAdapter adapter = new MyArrayAdapter();
		dropDownList.setAdapter(adapter);
		dropDownList.setOnItemClickListener(adapter);

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

	public void onResume() {
		super.onResume();
		if (isPreBack) {
			isPreBack = false;
		}
		// baidu code start
		StatService.onResume(this);
		// baidu code end
	}

	public void onPause() {
		super.onPause();
		// baidu code start
		StatService.onPause(this);
		// baidu code end
	}

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
			StatService.onEvent(this, "click_empty", "show ads by click");
		} else {
			int id = v.getId();
			switch (id) {
			case R.id.iv_mind_map_more:
				dropDownList.show(v);
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

	private void createMindMapImage() {
		isCancelCreateImage = false;
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在生成图片...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isCancelCreateImage = true;
			}
		});
		progressDialog.show();

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				final String path = createMindMapImageByThread(mindMap.mindMapId);
				if (!isCancelCreateImage) {
					Message message = mHandler.obtainMessage(MSG_CREATE_IMAGE);
					message.obj = path;
					message.sendToTarget();
				}
			}
		};

		new Thread(runnable).start();

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
		EditTextNode etn = (EditTextNode) findViewById((int) rootNode._id);
		mindMapPad.requestMoveToNode(etn);
		etn.requestFocus();

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

		Node moveNode = (Node) editTextNode.getTag();
		if (!moveNode.isRootNode) {
			notMoveToParentNodes = mindMap.getNotMoveToParent(moveNode);
		}
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
					if (notMoveToParentNodes.contains((Node) etnC.getTag())) {
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
							int margin = (int) EngineApplication.transformDP2PX(17);
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
			notMoveToParentNodes.clear();

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

	private class MyArrayAdapter extends BaseAdapter implements OnItemClickListener {
		String[] items = { "管理", "根节点", "清空节点", "导出图片" };

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater li = LayoutInflater.from(MindMapActivity.this);
				convertView = li.inflate(R.layout.drop_down_item, null);
			}
			String item = items[position];
			TextView textItem = (TextView) convertView.findViewById(R.id.text_item);
			textItem.setText(item);
			textItem.setTag(position);
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> adaterView, View view, int positon, long index) {
			switch (positon) {
			case 0:
				Intent intent = new Intent(MindMapActivity.this, MMManagerActivity.class);
				startActivityForResult(intent, REQUST_CODE_MANAGE_MINDMAP);
				break;
			case 1:
				moveToRootNode();
				break;
			case 2:
				clearNodes();
				break;
			case 3:
				createMindMapImage();
				break;
			default:
				break;
			}
			dropDownList.dismiss();
		}

	}

	private void processCreatedImage(final String path) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (path != null) {
			String message = getString(R.string.hint_created_image);
			Formatter ft = new Formatter().format(message, mindMap.name, path);
			DialogUtils.showHintDilog(this, ft.toString(), getString(R.string.preview), getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_POSITIVE) {
								File file = new File(path);
								Uri uri = Uri.fromFile(file);
								Intent intent = new Intent();
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setDataAndType(uri, "image/jpeg");
								try {
									startActivity(intent);
									overridePendingTransition(R.anim.right_in_activity, R.anim.left_out_activity);
								} catch (ActivityNotFoundException e) {
									Toast.makeText(MindMapActivity.this, "没有找到查看图片的应用", Toast.LENGTH_LONG).show();
								}
							}
						}
					});
		} else {
			String message = getString(R.string.hint_failed_create_image);
			Formatter ft = new Formatter().format(message, mindMap.name);
			Toast.makeText(this, ft.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private String createMindMapImageByThread(long mindMapId) {
		MindMapManager mmManager = new MindMapManager();
		MindMap tempMindMap = mmManager.getMindMapBy(mindMapId);
		MindMapView tempMMView = new MindMapView(this);

		List<Node> nodeList = tempMindMap.getNodes();
		for (Node node : nodeList) {
			EditTextNode nl = new EditTextNode(this);
			nl.setNode(node);
			tempMMView.addView(nl);
		}

		if (isCancelCreateImage) {
			return null;
		}

		tempMMView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		int top = 0;
		int bottom = 0;
		int left = 0;
		int right = 0;

		for (Node node : nodeList) {
			View nodeView = tempMMView.findViewById((int) node._id);
			int cTop = (int) node.y;
			int cBottom = (int) node.y + nodeView.getMeasuredHeight();

			int cLeft = (int) node.x;
			int cRight = (int) node.x + nodeView.getMeasuredWidth();

			top = top < cTop ? top : cTop;
			bottom = bottom > cBottom ? bottom : cBottom;
			left = left < cLeft ? left : cLeft;
			right = right > cRight ? right : cRight;

			if (!node.isRootNode) {
				LinkView lv = new LinkView(tempMMView, node);
				tempMMView.addView(lv, 0);
			}
		}

		if (isCancelCreateImage) {
			return null;
		}

		tempMMView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		int width = right - left;
		int height = bottom - top;

		int scrollX = 0;
		int scrollY = 0;

		if (width <= 800) {
			scrollX = left - (840 - width) / 2;
			width = 840;
		} else {
			width += 40;
			scrollX = left - 20;
		}

		if (height <= 800) {
			scrollY = top - (840 - height) / 2;
			height = 840;
		} else {
			height += 40;
			scrollY = top - 20;
		}

		tempMMView.layout(left, top, right, bottom);

		tempMMView.scrollTo(scrollX, scrollY);

		if (isCancelCreateImage) {
			return null;
		}
		Bitmap bitmap = BitmapUtils.convertViewToBitmap(tempMMView, width, height);

		if (bitmap != null && !isCancelCreateImage) {

			String fileName = tempMindMap.name + "_" + System.currentTimeMillis() + ".jpeg";
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/M_mindmap";

			try {
				FileUtils.createFile(path, ".NOMEDIA");
				File file = FileUtils.createFile(path, fileName);
				OutputStream os = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.close();
				bitmap.recycle();
				return file.getAbsolutePath();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

}
