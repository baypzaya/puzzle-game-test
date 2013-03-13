package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class EditTextNode extends EditText implements INode {
	private float x;
	private float y;

//	public EditTextNode(Context context, Node node) {
//		super(context);
//		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		setLayoutParams(layout);
//		setId((int)node._id);
//		this.x = node.x;
//		this.y = node.y;
//		setTitle(node.title);
//	}
	
	

	public EditTextNode(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	@Override
	public void setTitle(String title) {
		setText(title);
	}
	
	@Override
	public String getTitle() {
		return getEditableText().toString();
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

	public void setEditEnable(boolean b) {
		if (b) {
			setKeyListener(TextKeyListener.getInstance());
		} else {
			setKeyListener(null);
		}

	}

}
