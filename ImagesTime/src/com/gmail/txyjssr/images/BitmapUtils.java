package com.gmail.txyjssr.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class BitmapUtils {

	/** 获取文件指定尺寸的bitmap */
	public static Bitmap getBitmapBy(String path, int width, int height) {
		//仅获取图片边界
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int widthB = options.outWidth;
		
		//配置图片参数，简化图片内存占用
		options.inSampleSize = widthB/width;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		
		//获取缩略图
		Bitmap bitmapTarge   = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT); 
		
		return bitmapTarge;
	}

}
