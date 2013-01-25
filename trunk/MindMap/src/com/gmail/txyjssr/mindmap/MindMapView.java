package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MindMapView extends FrameLayout {
	private final int TOUCH_MODE_NONE = 0;
	private final int TOUCH_MODE_SINGLE = 1;
	private final int TOUCH_MODE_DOUBLE = 2;

	private float downX;
	private float downY;
	private Camera camera = new Camera();
	private float distancePoints = 0;

	private float currentScale = 1f;
	private int lastTouchMode = TOUCH_MODE_NONE;

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
	// canvas.save();
	// Matrix matrix = new Matrix();
	// camera.save();
	// camera.translate(0, 0, 300f);
	// camera.getMatrix(matrix);
	// camera.restore();
	//
	// canvas.concat(matrix);
	// super.onDraw(canvas);
	//
	// Paint roundPaint = new Paint();
	// roundPaint.setColor(Color.RED);
	// Rect rectF = new Rect(20, 20, 400, 400);
	// canvas.drawRect(rectF, roundPaint);
	// canvas.restore();
	// }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);

			int centerX = 0;
			int centerY = 0;
			int width = 0;
			int height = 0;

			if (child instanceof NodeView) {
				NodeView nodeView = (NodeView) child;
				centerX = (r - l) / 2 + (int) (nodeView.getPointX() * currentScale);
				centerY = (b - t) / 2 + (int) (nodeView.getPointY() * currentScale);
				width = (int) (child.getMeasuredWidth() * currentScale / 2);
				height = (int) (child.getMeasuredHeight() * currentScale / 2);
				child.layout(centerX - width, centerY - height, centerX + width, centerY + height);
			} else if (child instanceof LinkView) {
				LinkView linkView = (LinkView) child;
				int childCenterX = (int) ((linkView.startX + linkView.endX) / 2 * currentScale);
				int childCenterY = (int) ((linkView.startY + linkView.endY) / 2 * currentScale);
				centerX = (r - l) / 2 + childCenterX;
				centerY = (b - t) / 2 + childCenterY;
				width = (int) (linkView.getMeasuredWidth() * currentScale / 2);
				height = (int) (linkView.getMeasuredHeight() * currentScale / 2);

			}

			child.layout(centerX - width, centerY - height, centerX + width, centerY + height);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("yujsh log", "pointerCount:" + event.getPointerCount());
		if (event.getPointerCount() == 1) {
			if (lastTouchMode != TOUCH_MODE_SINGLE) {
				distancePoints = 0;
				downX = event.getX();
				downY = event.getY();
				lastTouchMode = TOUCH_MODE_SINGLE;
			}
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
				downX = moveX;
				downY = moveY;
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return true;
		} else if (event.getPointerCount() == 2) {
			if (lastTouchMode != TOUCH_MODE_DOUBLE) {
				lastTouchMode = TOUCH_MODE_DOUBLE;
			}
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				float downX0 = event.getX(0);
				float downY0 = event.getY(0);
				float downX1 = event.getX(1);
				float downY1 = event.getY(1);
				distancePoints = (float) Math.sqrt((downX0 - downX1) * (downX0 - downX1) + (downY0 - downY1)
						* (downY0 - downY1));
				Log.i("yujsh log", "distancePoints:" + distancePoints);
				break;
			case MotionEvent.ACTION_MOVE:
				float moveX0 = event.getX(0);
				float moveY0 = event.getY(0);
				float moveX1 = event.getX(1);
				float moveY1 = event.getY(1);
				float moveDistancePoints = (float) Math.sqrt((moveX0 - moveX1) * (moveX0 - moveX1) + (moveY0 - moveY1)
						* (moveY0 - moveY1));
				Log.i("yujsh log", "moveDistancePoints:" + moveDistancePoints);
				if (distancePoints != 0) {
					float scale = moveDistancePoints / distancePoints;
					currentScale = scale;
					currentScale = currentScale > 3 ? 3 : currentScale;
					currentScale = currentScale < 0.3f ? 0.3f : currentScale;
					Log.i("yujsh log", "currentScale:" + currentScale);
					requestLayout();
				} else {
					distancePoints = moveDistancePoints / currentScale;
				}
				// invalidate();
				break;
			case MotionEvent.ACTION_UP:
				distancePoints = 0;
				break;
			}
			return true;
		}
		lastTouchMode = TOUCH_MODE_NONE;
		return false;
	}
}
