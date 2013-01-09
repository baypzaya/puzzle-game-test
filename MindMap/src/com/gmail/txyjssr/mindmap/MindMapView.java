package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MindMapView extends View {

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
		canvas.drawColor(Color.WHITE);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap = getBitmap("中文测试京东分开就撒的");
		canvas.drawBitmap(bitmap, 100, 100, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	private Bitmap getBitmap(String text) {
		Bitmap bitmap = new Node().getNodeBitmap(text);
		return bitmap;
	}
}
