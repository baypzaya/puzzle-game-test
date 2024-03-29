package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

public class NodeView extends View implements INode {

	private final int padding = 16;
	private final int roundStrokeWidth = 2;
	private final int titleSize = 20;

	private String title;
	private int type;
	private float x;
	private float y;
	private Rect roundRect;
	private Paint tilePaint;
	private Paint roundPaint;
	private Bitmap bitmap;

	// private Camera camera = new Camera();

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

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (changed) {

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Rect roundRect = new Rect();
		tilePaint.getTextBounds(title, 0, title.length(), roundRect);

		int titleWidth = roundRect.width() < 50 ? 50 : roundRect.width();
		int titleHeight = roundRect.height() < 20 ? 20 : roundRect.height();

		int roundWidth = titleWidth + 2 * padding;
		int roundHeight = titleHeight + 2 * padding;

		setMeasuredDimension(roundWidth + 2 * roundStrokeWidth, roundHeight + 2 * roundStrokeWidth);
	}

	private Bitmap getNodeBitmap(String title) {
		Rect roundRect = new Rect();
		tilePaint.getTextBounds(title, 0, title.length(), roundRect);

		Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		int centerX = bitmap.getWidth() / 2;
		int centerY = bitmap.getHeight() / 2;

		Canvas canvas = new Canvas(bitmap);

		RectF rectF = new RectF(roundStrokeWidth, roundStrokeWidth, getMeasuredWidth() - roundStrokeWidth,
				getMeasuredHeight() - roundStrokeWidth);
		canvas.drawRoundRect(rectF, 4, 4, roundPaint);

		canvas.translate(centerX - roundRect.centerX(), centerY - roundRect.centerY());
		canvas.drawText(title, 0, 0, tilePaint);
		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (bitmap == null) {
			bitmap = getNodeBitmap(title);
		}

		float layoutWidth = Math.abs(getRight() - getLeft());
		float scale = layoutWidth / getMeasuredWidth();

		canvas.save();
		canvas.scale(scale, scale);
		canvas.drawColor(Color.LTGRAY);

		canvas.drawBitmap(bitmap, 0, 0, paint);
		canvas.restore();
	}

	/* (non-Javadoc)
	 * @see com.gmail.txyjssr.mindmap.INode#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
		requestLayout();
		invalidate();
	}

	/* (non-Javadoc)
	 * @see com.gmail.txyjssr.mindmap.INode#setLocation(float, float)
	 */
	@Override
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
		invalidate();
	}

	/* (non-Javadoc)
	 * @see com.gmail.txyjssr.mindmap.INode#getPointX()
	 */
	@Override
	public float getPointX() {
		return x;
	}

	/* (non-Javadoc)
	 * @see com.gmail.txyjssr.mindmap.INode#getPointY()
	 */
	@Override
	public float getPointY() {
		return y;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
