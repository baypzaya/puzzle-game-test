package com.gmail.txyjssr.mindmap.test;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.ImageView;

import com.gmail.txyjssr.mindmap.EditTextNode;
import com.gmail.txyjssr.mindmap.MindMap;
import com.gmail.txyjssr.mindmap.MindMapActivity;
import com.gmail.txyjssr.mindmap.Node;
import com.gmail.txyjssr.mindmap.NodeLayout;
import com.gmail.txyjssr.mindmap.R;
import com.gmail.txyjssr.mindmap.test.utils.ReflectUtils;

public class MMATest extends InstrumentationTestCase {

	MindMapActivity mmActivity;
	private ImageView ivRoot;
	private ImageView ivClear;
	private ImageView ivDelete;
	private ImageView ivAdd;
	private ImageView ivEdit;

	protected void setUp() throws Exception {
		super.setUp();

		Intent intent = new Intent();
		intent.setClassName("com.gmail.txyjssr.mindmap", "com.gmail.txyjssr.mindmap.MindMapActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mmActivity = (MindMapActivity) getInstrumentation().startActivitySync(intent);
		ivRoot = (ImageView) mmActivity.findViewById(R.id.iv_root);
		ivClear = (ImageView) mmActivity.findViewById(R.id.iv_clear);
		ivDelete = (ImageView) mmActivity.findViewById(R.id.iv_delete);
		ivAdd = (ImageView) mmActivity.findViewById(R.id.iv_add);
		ivEdit = (ImageView) mmActivity.findViewById(R.id.iv_edit);
	}

	protected void tearDown() throws Exception {
//		mmActivity.finish();
		super.tearDown();
	}

	public void testAddNode() {
		final MindMap mindMap = (MindMap) ReflectUtils.getField(mmActivity, "mindMap");
		final int lNodeCount = mindMap.nodeList.size();

		Node rootNode = mindMap.getRootNode();
		for (int i = 0; i < 5; i++) {
			addNode(rootNode, "child_" + rootNode.title);
		}
		
		for(Node node:rootNode.nodeChildren){
			for (int i = 0; i < 5; i++) {
				addNode(node, "child_" + node.title);
			}
			
			for(Node tnode:node.nodeChildren){
				for (int i = 0; i < 4; i++) {
					addNode(tnode, "child_" + node.title);
				}
			}
		}

		int cNodeCount = mindMap.nodeList.size();
		Log.i("yusjh log", "lNodeCount:" + lNodeCount + " cNodeCount" + cNodeCount);

		assertEquals(cNodeCount - lNodeCount, 100);
	}

	private void addNode(Node node, String nodeName) {
		Runnable r = ReflectUtils.getPostRunnable(mmActivity, "addNode(Node,String)", new Object[] { node, nodeName});
//		mmActivity.runOnUiThread(r);
		r.run();
	}

}
