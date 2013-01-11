package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MindMapView extends ViewGroup {

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
	// canvas.drawColor(Color.WHITE);
	// int childCount = getChildCount();
	// for(int i=0;i<childCount;i++){
	// getChildAt(i).onDraw(canvas);
	// }
	// }

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		float x = event.getX();
//		float y = event.getY();
//		Log.i("yujsh log", "event type:" + event.getAction());
//		Log.i("yujsh log", "event x:" + x + " y:" + y);
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			break;
//		case MotionEvent.ACTION_MOVE:
//			break;
//		case MotionEvent.ACTION_UP:
//			break;
//		}
//		return true;
//	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			child.layout(l, t, r, b);
		}

	}
}
