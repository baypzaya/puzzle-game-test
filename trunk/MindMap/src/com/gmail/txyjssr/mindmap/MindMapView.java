package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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
		canvas.translate(getMeasuredWidth()/2f, getMeasuredHeight()/2f);
		canvas.save();
		super.onDraw(canvas);
		canvas.restore();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);

			if (child instanceof NodeView) {
				NodeView nodeView = (NodeView) child;
				int centerX = (r - l) / 2 + (int) nodeView.getX();
				int centerY = (b - t) / 2 + (int) nodeView.getY();
				int width = child.getMeasuredWidth() / 2;
				int height = child.getMeasuredHeight() / 2;
				child.layout(centerX + -width, centerY - height, centerX + width, centerY + height);
			} else if(child instanceof LinkView){
				LinkView linkView = (LinkView) child;
				int centerX = (r - l) / 2;
				int centerY = (b - t) / 2;
				Log.i("yujsh log","l:"+centerX + (int)linkView.startX);
				Log.i("yujsh log","t:"+centerY + (int)linkView.startY);
				Log.i("yujsh log","r:"+centerX + (int)linkView.endX);
				Log.i("yujsh log","b:"+centerY + (int)linkView.endY);
				child.layout(centerX + (int)linkView.startX-25, centerY + (int)linkView.startY-25, centerX + (int)linkView.endX+25, centerY + (int)linkView.endY+25);
//				child.layout(l,t,r,b);
			}
		}

	}
}
