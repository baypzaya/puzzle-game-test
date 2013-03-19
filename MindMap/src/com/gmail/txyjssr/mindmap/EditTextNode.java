package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.text.InputType;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class EditTextNode extends EditText implements INode {
	private float x;
	private float y;
	private OnMoveListener mMoveListener;
	
	private int lastInputType = InputType.TYPE_NULL;

	// public EditTextNode(Context context, Node node) {
	// super(context);
	// LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT);
	// setLayoutParams(layout);
	// setId((int)node._id);
	// this.x = node.x;
	// this.y = node.y;
	// setTitle(node.title);
	// }

	public void setOnMoveListener(OnMoveListener onMoveListener) {
		this.mMoveListener = onMoveListener;
	}

	public EditTextNode(Context context, AttributeSet attrs) {
		super(context, attrs);
		lastInputType = getInputType();
		setInputType(InputType.TYPE_NULL);
	}

	private float motionX;
	private float motionY;
	private int currentModel = 0;

	private final int moveTouch = 1;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			currentModel = 0;
			motionX = event.getX();
			motionY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float tx = event.getX();
			float ty = event.getY();
			if (currentModel == 0 && (Math.abs(tx - motionX) > 20 || Math.abs(ty - motionY) > 20)) {
					currentModel = moveTouch;
			}
			
			if(currentModel == moveTouch){
				x = x+tx - motionX;
				y = y+ty - motionY;
				if(mMoveListener!=null){
					mMoveListener.onMove(this);
				}
				requestLayout();
			}
			break;
		case MotionEvent.ACTION_UP:	
			if(currentModel == moveTouch){
				if(mMoveListener!=null){
					mMoveListener.endMove(this);
				}
			}
			break;
		}
		if(currentModel == 0){
			return super.onTouchEvent(event);
		}else{
			return true;
		}
		
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

//	public void setEditEnable(boolean b) {
//		if (b) {
//			setInputType(lastInputType);
//			setKeyListener(TextKeyListener.getInstance());
//		} else {
//			setKeyListener(null);
//			setInputType(InputType.TYPE_NULL);
//		}

//	}
	
	public interface OnMoveListener{
		public void onMove(EditTextNode etn);
		public void endMove(EditTextNode etn);
	}

}
