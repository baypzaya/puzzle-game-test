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
		Bitmap bitmap = getBitmap("test");
		canvas.drawBitmap(bitmap, 100, 100, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	private Bitmap getBitmap(String text) {
		Paint mFramePaint = new Paint();
		mFramePaint.setColor(Color.RED);
		mFramePaint.setAntiAlias(true);
		mFramePaint.setStyle(Paint.Style.STROKE);
		mFramePaint.setStrokeWidth(2);

		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(5);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setTextSize(64);
		mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

		Rect rect = new Rect();
		mPaint.getTextBounds(text, 0, text.length(), rect);

		Bitmap bitmap = Bitmap.createBitmap(rect.width() + 6, rect.height() + 6, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		c.drawColor(Color.GRAY);

		c.translate(0, rect.height()+3);
		
		Log.i("yujsh log","text center:"+rect.centerX()+" "+rect.centerY());

		// mPaint.setColor(0xFF88FF88);
		// c.drawRect(rect, mFramePaint);
		// mPaint.setColor(Color.BLACK);
		// c.drawText(text, 0, 0, mPaint);

		RectF rectf = new RectF(rect);
		c.drawRoundRect(rectf, 5, 5, mFramePaint);
		c.drawText(text, 0, 0, mPaint);
		return bitmap;
	}
}
