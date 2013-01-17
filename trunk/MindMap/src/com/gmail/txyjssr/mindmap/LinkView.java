package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class LinkView extends View{
	public float startX;
	public float startY;
	public float endX;
	public float endY;

	public LinkView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(20);
		canvas.drawLine(startX, startY, endX, endY, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int)Math.abs(endX-startX), (int)Math.abs(endY-startY));
	}

	public void setLink(float x, float y, float x2, float y2) {
		startX = x;
		startY = y;
		endX = x2;
		endY = y2;
	}
	
	
	
}
