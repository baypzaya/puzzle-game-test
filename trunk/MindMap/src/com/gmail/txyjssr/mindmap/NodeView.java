package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class NodeView extends TextView {

	private final int padding = 16;
	private final int roundStrokeWidth = 2;
	private final int titleSize = 40;

	private String title;
	private int type;
	private float x;
	private float y;
	private Rect roundRect;
	private Paint tilePaint;
	private Paint roundPaint;
	private Bitmap bitmap;

	public NodeView(Context context) {
		super(context);

		roundPaint = new Paint();
		roundPaint.setColor(Color.RED);
		roundPaint.setAntiAlias(true);
		roundPaint.setStyle(Paint.Style.STROKE);
		roundPaint.setStrokeWidth(roundStrokeWidth);

		tilePaint = new Paint();
		tilePaint.setAntiAlias(true);
		tilePaint.setStrokeCap(Paint.Cap.ROUND);
		tilePaint.setTextSize(titleSize);
		tilePaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC));

	}

	public NodeView(Context context, Node node) {
		this(context);
		this.title = node.title;
		this.x = node.x;
		this.y = node.y;
	}

	private Bitmap getNodeBitmap(String title) {
		Log.i("yujsh log", "title:" + title);
		Rect roundRect = new Rect();
		tilePaint.getTextBounds(title, 0, title.length(), roundRect);

		int titleWidth = roundRect.width() < 50 ? 50 : roundRect.width();
		int titleHeight = roundRect.height() < 100 ? 100 : roundRect.height();

		int roundWidth = titleWidth + 2 * padding;
		int roundHeight = titleHeight + 2 * padding;

		Bitmap bitmap = Bitmap.createBitmap(roundWidth + 2 * roundStrokeWidth, roundHeight + 2 * roundStrokeWidth,
				Bitmap.Config.ARGB_8888);
		int centerX = bitmap.getWidth() / 2;
		int centerY = bitmap.getHeight() / 2;

		Canvas canvas = new Canvas(bitmap);

		canvas.drawColor(Color.CYAN);

		RectF rectF = new RectF(roundStrokeWidth, roundStrokeWidth, roundWidth + roundStrokeWidth, roundHeight
				+ roundStrokeWidth);
		canvas.drawRoundRect(rectF, 4, 4, roundPaint);

		canvas.translate(centerX - roundRect.centerX(), centerY - roundRect.centerY());
		canvas.drawText(title, 0, 0, tilePaint);
		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.translate(canvas.getWidth() / 2f, canvas.getHeight() / 2f);
		canvas.save();
		if(bitmap==null){
			bitmap = getNodeBitmap(title);
		}
		canvas.drawBitmap(bitmap, (x - bitmap.getWidth()) / 2f, (y - bitmap.getHeight()) / 2f, paint);
		canvas.restore();
	}

	public void setTitle(String title) {
		this.title = title;
		bitmap = getNodeBitmap(title);
		invalidate();
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		invalidate();
	}

}
