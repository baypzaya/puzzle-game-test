package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.TextView;

public class EditTextNode extends TextView implements INode {
	private float x;
	private float y;
	private OnMoveListener mMoveListener;

	public void setOnMoveListener(OnMoveListener onMoveListener) {
		this.mMoveListener = onMoveListener;
	}
	
	public EditTextNode(Context context){
		super(context);
	}

	public EditTextNode(MindMapActivity mmActivity) {
		super(mmActivity);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnFocusChangeListener(mmActivity);
		this.setOnMoveListener(mmActivity);
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
				if (mMoveListener != null) {
					mMoveListener.startMove(this);
				}

			}

			if (currentModel == moveTouch) {
				x = x + tx - motionX;
				y = y + ty - motionY;
				Log.i("yujsh log","x:"+tx+" y:"+ty);
				LayoutParams p = (LayoutParams) getLayoutParams();
				p.x = (int) x;
				p.y = (int) y;
				if (mMoveListener != null) {
					mMoveListener.onMove(this);
				}
				requestLayout();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (currentModel == moveTouch) {
				if (mMoveListener != null) {
					mMoveListener.endMove(this);
				}
			} else {
				long downTime = event.getDownTime();
				long eventTime = event.getEventTime();
				if (eventTime - downTime < 500) {
					requestFocus();
				}
			}
			break;
		}

		return true;

	}

	@Override
	public void setTitle(String title) {
		setText(title);
	}

	@Override
	public String getTitle() {
		return getText().toString();
	}

	@Override
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (int) x, (int) y));
	}

	@Override
	public float getPointX() {
		return x;
	}

	@Override
	public float getPointY() {
		return y;
	}

	public interface OnMoveListener {
		public void onMove(EditTextNode etn);

		public void startMove(EditTextNode editTextNode);

		public void endMove(EditTextNode etn);
	}

	public void setNode(Node node) {
		this.setId((int) node._id);
		this.setTitle(node.title);
		this.setLocation(node.x, node.y);
		setTag(node);
		updateEditTextNodeColor(node);
	}
	
	private void updateEditTextNodeColor(Node node){
		int layerNum = node.getLayerNum();
		switch (layerNum) {
		case 5:
			setBackgroundResource(R.drawable.node_status_red_selector);
			break;
		case 1:
			setBackgroundResource(R.drawable.node_status_green_selector);
			break;
		case 2:
			setBackgroundResource(R.drawable.node_status_orange_selector);
			break;
		case 4:
			setBackgroundResource(R.drawable.node_status_blue_selector);
			break;
		case 3:
			setBackgroundResource(R.drawable.node_status_purpel_selector);
			break;
		default:
			setBackgroundResource(R.drawable.node_status_yellow_selector);
			break;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		updateEditTextNodeColor((Node)getTag());
		super.onLayout(changed, left, top, right, bottom);
	}

	public boolean containPoint(int x, int y) {
		int top = getTop();
		int bottom = getBottom();
		int left = getLeft();
		int right = getRight();

		return left < x && x < right && top < y && y < bottom;
	}
}
