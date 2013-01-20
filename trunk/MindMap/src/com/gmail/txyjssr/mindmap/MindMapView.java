package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MindMapView extends FrameLayout {

	private float downX;
	private float downY;

	private float currentScale = 1;

	public MindMapView(Context context) {
		super(context);
	}

	public MindMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// canvas.translate(getMeasuredWidth()/2f, getMeasuredHeight()/2f);
	// canvas.save();
	// super.onDraw(canvas);
	// canvas.restore();
	// }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);

			if (child instanceof NodeView) {
				NodeView nodeView = (NodeView) child;
				int centerX = (r - l) / 2 + (int) nodeView.getPointX();
				int centerY = (b - t) / 2 + (int) nodeView.getPointY();
				int width = child.getMeasuredWidth() / 2;
				int height = child.getMeasuredHeight() / 2;
				child.layout(centerX - width, centerY - height, centerX + width, centerY + height);
			} else if (child instanceof LinkView) {
				LinkView linkView = (LinkView) child;
				int centerX = (r - l) / 2;
				int centerY = (b - t) / 2;
				int childL = centerX + (int) linkView.startX - 15;
				int childT = centerY + (int) linkView.startY - 15;
				int childR = centerX + (int) linkView.endX + 15;
				int childB = centerY + (int) linkView.endY + 15;
				Log.i("yujsh log", "childL:" + childL + "childT:" + childT + "childR:" + childR + "childB:" + childB);
				int tempL = childL <= childR ? childL : childR;
				int tempR = childL >= childR ? childL : childR;
				int tempT = childT <= childB ? childT : childB;
				int tempB = childT >= childB ? childT : childB; 
				child.layout(tempL, tempT, tempR, tempB);
				// child.layout(l,t,r,b);
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() == 1) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float moveX = event.getX();
				float moveY = event.getY();
				scrollBy((int) (downX - moveX), (int) (downY - moveY));
				// requestLayout();
				// invalidate();
				downX = moveX;
				downY = moveY;
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
		} else {
			return false;
		}
	}
}
