package com.gmail.txyjssr.mindmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

public class BitmapUtils {
	private static final float MAX_BITMAP_WIDTH = 2048;
	private static final float MAX_BITMAP_HEIGHT = 2048;

	public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
		float scaleX = 1;
		float scaleY = 1;
		if (bitmapWidth > MAX_BITMAP_WIDTH) {
			scaleX = MAX_BITMAP_WIDTH / bitmapWidth;
		}

		if (bitmapHeight > MAX_BITMAP_HEIGHT) {
			scaleY = MAX_BITMAP_HEIGHT / bitmapHeight;
		}

		float scale = scaleX < scaleY ? scaleX : scaleY;
		Bitmap bitmap = null;
		try {
			int width = (int) (bitmapWidth * scale);
			int height = (int) (bitmapHeight * scale);
			bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawColor(Color.WHITE);
			canvas.translate(-view.getScrollX() * scale, -view.getScrollY() * scale);
			canvas.scale(scale, scale);
			view.draw(canvas);
		} catch (Exception e) {
			Log.e("M_mindmap", e.getLocalizedMessage());
			bitmap = null;
		}

		return bitmap;
	}
}
