package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MindMapView extends FrameLayout {
	private final int TOUCH_MODE_NONE = 0;
	private final int TOUCH_MODE_SINGLE = 1;
	private final int TOUCH_MODE_DOUBLE = 2;

	private float downX;
	private float downY;
	private float distancePoints = 0;

	private float currentScale = 1f;
	private int lastTouchMode = TOUCH_MODE_NONE;

	public MindMapView(Context context) {
		super(context);
	}

	public MindMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);

			int centerX = 0;
			int centerY = 0;
			int width = 0;
			int height = 0;

			if (child instanceof INode) {
				INode nodeView = (INode) child;
				centerX = (r - l) / 2 + (int) (nodeView.getPointX() * currentScale);
				centerY = (b - t) / 2 + (int) (nodeView.getPointY() * currentScale);
				width = (int) (child.getMeasuredWidth() * currentScale / 2);
				height = (int) (child.getMeasuredHeight() * currentScale / 2);
				child.layout(centerX - width, centerY - height, centerX + width, centerY + height);
			} else if (child instanceof LinkView) {
				LinkView linkView = (LinkView) child;
				int childCenterX = (int) ((linkView.parentX + linkView.childX) / 2 * currentScale);
				int childCenterY = (int) ((linkView.parentY + linkView.childY) / 2 * currentScale);
				centerX = (r - l) / 2 + childCenterX;
				centerY = (b - t) / 2 + childCenterY;
				width = (int) (linkView.getMeasuredWidth() * currentScale / 2);
				height = (int) (linkView.getMeasuredHeight() * currentScale / 2);
			}

			child.layout(centerX - width, centerY - height, centerX + width, centerY + height);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastTouchMode = TOUCH_MODE_NONE;
			distancePoints = 0;
			downX = event.getX();
			downY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() == 1) {
				float moveX = event.getX();
				float moveY = event.getY();
				
				if (lastTouchMode == TOUCH_MODE_NONE && (Math.abs(downX - moveX) > 20 || Math.abs(downY - moveY) > 20)) {
					lastTouchMode = TOUCH_MODE_SINGLE;
				}else if(lastTouchMode == TOUCH_MODE_DOUBLE){
					lastTouchMode = TOUCH_MODE_NONE;
					downX = moveX;
					downY = moveY;
				}
				
				if (lastTouchMode == TOUCH_MODE_SINGLE) {
					scrollBy((int) (downX - moveX), (int) (downY - moveY));
					downX = moveX;
					downY = moveY;
				}
			}else if (event.getPointerCount() == 2) {
//				lastTouchMode = TOUCH_MODE_DOUBLE;
//				float moveX0 = event.getX(0);
//				float moveY0 = event.getY(0);
//				float moveX1 = event.getX(1);
//				float moveY1 = event.getY(1);
//				float moveDistancePoints = (float) Math.sqrt((moveX0 - moveX1) * (moveX0 - moveX1) + (moveY0 - moveY1)
//						* (moveY0 - moveY1));
//				if (distancePoints != 0) {
//					float scale = moveDistancePoints / distancePoints;
//					currentScale = scale;
//					currentScale = currentScale > 3 ? 3 : currentScale;
//					currentScale = currentScale < 0.3f ? 0.3f : currentScale;
//					requestLayout();
//				} else {
//					distancePoints = moveDistancePoints / currentScale;
//				}
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		if (lastTouchMode == TOUCH_MODE_NONE) {
			return super.onTouchEvent(event);
		} else {
			return true;
		}
	}

	public void moveToNodeLocation(float x, float y) {
		scrollTo((int)x, (int)y);
	}
}
