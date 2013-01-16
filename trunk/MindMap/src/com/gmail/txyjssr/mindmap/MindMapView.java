package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MindMapView extends FrameLayout {

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
	protected void onDraw(Canvas canvas) {
		//canvas.save();
		super.onDraw(canvas);
		//canvas.restore();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			
			int centerX=(r-l)/2;
			int centerY=(b-t)/2;
			int width = child.getMeasuredWidth()/2;
			int height = child.getMeasuredHeight()/2;
			child.layout(centerX-width, centerY-height, centerX+width, centerY+height);
			
		}

	}
}
