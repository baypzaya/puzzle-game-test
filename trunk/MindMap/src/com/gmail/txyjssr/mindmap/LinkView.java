package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

public class LinkView extends View {
	private final int strokeWidth = 10;

	public float startX;
	public float startY;
	public float endX;
	public float endY;

	private Paint paint;

	public LinkView(Context context) {
		super(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(20);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.YELLOW);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(strokeWidth);

		int padding = strokeWidth / 2;
		canvas.drawLine(0 + padding, 0 + padding, getMeasuredWidth() - padding, getMeasuredHeight() - padding, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) Math.abs(endX - startX) + strokeWidth, (int) Math.abs(endY - startY) + strokeWidth);
	}

	public void setLink(float x, float y, float x2, float y2) {
		startX = x;
		startY = y;
		endX = x2;
		endY = y2;
	}

}
