package com.idreamsky.ktouchread.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {

	public static Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * get Bitmap from the uri string
	 * 
	 * @author Abner.Wu
	 * @date 2010-9-21
	 * 
	 */
	public static Bitmap getBitmapFromUri(String strUri) {
		URL imageUrl;
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			// Create a Drawable by decoding a stream from a remote URL
			imageUrl = new URL(strUri);
			is = imageUrl.openStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	
	/**
	 * load Drawable image from drawableId
	 * 
	 * @param drawableId the id of the Drawable id
	 * @param context Context
	 * 
	 * @author Abner.Wu
	 * @date 2010-9-28
	 */
	public static Drawable loadDrawable(int drawableId,Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),drawableId);
		Drawable drawable = null;
		if (bitmap != null) {
			drawable = new BitmapDrawable(bitmap);
		}
		return drawable;
	}
	
	
	/**
	 * 
	 * @param pathName
	 * @return
	 */
	public static Bitmap getBitmapFromBytes(byte[] dataByte) {
		Bitmap bmp = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(dataByte, 0, dataByte.length, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeByteArray(dataByte, 0, dataByte.length, opts);;
		} catch (OutOfMemoryError err) {
			if(Configuration.DEBUG_VERSION){
				err.printStackTrace();
			}
		}
		return bmp;
	}
	
	/**
	 * 
	 * @param pathName
	 * @return
	 */
	public static Bitmap getBitmapFromPath(String pathName) {
		Bitmap bmp = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(pathName, opts);
		} catch (OutOfMemoryError err) {
			if(Configuration.DEBUG_VERSION){
				err.printStackTrace();
			}
		}
		return bmp;
	}
	
	public static Bitmap getBitmapFromRes(Context mContext,int resourceId) {
		Bitmap bmp = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(mContext.getResources(),resourceId,opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeResource(mContext.getResources(),resourceId);
		} catch (OutOfMemoryError err) {
			if(Configuration.DEBUG_VERSION){
				err.printStackTrace();
			}
		}
		return bmp;
	}
	

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		try {
			int width = bitmap.getWidth(), height = bitmap.getHeight();
			Bitmap output = Bitmap
					.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, width, height);
			RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			canvas = null;
			paint = null;
			rect = null;
			rectF = null;
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}

			return output;

		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
	}
	
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8 ) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
}
