package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class EditTextNode extends EditText implements INode {
	private float x;
	private float y;
	private String title;
	private boolean defaultEditable = false;
	private MovementMethod defaultMovementMethod = null;

	public EditTextNode(Context context, Node node) {
		super(context);
		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(layout);
		setId(node.id);
		this.x = node.x;
		this.y = node.y;
		setTitle(node.title);
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		setText(title);
	}

	@Override
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public float getPointX() {
		return x;
	}

	@Override
	public float getPointY() {
		return y;
	}

	@Override
	protected boolean getDefaultEditable() {
		Log.i("yujsh log","getDefaultEditable");
		return defaultEditable;
	}

	@Override
	protected MovementMethod getDefaultMovementMethod() {
		Log.i("yujsh log","getDefaultMovementMethod");
		return defaultMovementMethod;
	}

	public void setEditEnable(boolean b) {
		defaultEditable = b;
		if (b) {
			defaultMovementMethod = ArrowKeyMovementMethod.getInstance();
			 setKeyListener(TextKeyListener.getInstance());
		} else {
			defaultMovementMethod = null;
			setKeyListener(null);
		}
		setMovementMethod(defaultMovementMethod);
	}

}
