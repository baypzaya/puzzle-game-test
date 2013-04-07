package com.gmail.txyjssr.mindmap;

import com.gmail.txyjssr.mindmap.EditTextNode.OnMoveListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NodeLayout extends LinearLayout implements INode, OnClickListener {

	private EditTextNode etn;
	private ImageView ivAdd;
	private ImageView ivDelete;

	private Node node;
	private OnButtonClickListener listener;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
		etn.setTag(node);
	}

	public NodeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.node, this, true);
		initView();
	}

	public NodeLayout(Context context) {
		super(context);
		setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		LayoutInflater.from(context).inflate(R.layout.node, this, true);
		initView();
	}

	public NodeLayout(MindMapActivity mmActivity, Node node) {
		this(mmActivity);
		
		this.setId((int) node._id);
		this.setTitle(node.title);
		this.setLocation(node.x, node.y);

		this.setNode(node);
		this.setOnFocusChangeListener(mmActivity);
		this.setOnMoveListener(mmActivity);
	}

	private void initView() {
		etn = (EditTextNode) findViewById(R.id.etn_node);
		ivAdd = (ImageView) findViewById(R.id.iv_add);
		ivDelete = (ImageView) findViewById(R.id.iv_delete);
		ivAdd.setOnClickListener(this);
		ivDelete.setOnClickListener(this);
	}

	@Override
	public void setTitle(String title) {
		etn.setTitle(title);

	}

	@Override
	public void setLocation(float x, float y) {
		etn.setLocation(x, y);

	}

	@Override
	public float getPointX() {
		return etn.getPointX();
	}

	@Override
	public float getPointY() {
		return etn.getPointY();
	}

//	public void setEditEnable(boolean b) {
//		etn.setEditEnable(b);
//
//	}

	@Override
	public String getTitle() {
		return etn.getTitle();
	}
	
	public void setOnMoveListener(OnMoveListener onMoveListener) {
		etn.setOnMoveListener(onMoveListener);
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		etn.setOnFocusChangeListener(l);
	}
	
	public void setOnButtonListener(OnButtonClickListener listener){
		this.listener = listener;
	}
	
	
	public boolean requestEditFocus(){
		return etn.requestFocus();
	}

	@Override
	public void onClick(View v) {
		if(listener==null) return;
		switch (v.getId()) {
		case R.id.iv_add:
			listener.onAddClick(node);
			break;
		case R.id.iv_delete:
			listener.onDeleteClick(node);
			break;
		default:
			break;
		}
	}
	
	public void hideButtons(){
		ivAdd.setVisibility(View.GONE);
		ivDelete.setVisibility(View.GONE);
	}
	
	public void showButtons(){
		ivAdd.setVisibility(View.GONE);
		ivDelete.setVisibility(View.GONE);
	}

	public interface OnButtonClickListener {
		public void onAddClick(Node node);

		public void onDeleteClick(Node node);
	}

	public void setButtonVisible(int visible) {
		ivAdd.setVisibility(visible);
		ivDelete.setVisibility(visible);
	}

	public EditTextNode getEditNode() {
		// TODO Auto-generated method stub
		return etn;
	}
	
	@Override
	public void setBackgroundResource(int resid){
		etn.setBackgroundResource(resid);
	}



}
