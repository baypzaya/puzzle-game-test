package com.shine.core.utils;

import java.io.BufferedInputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapUtils {
	
	/**
	 * 获取图片资源
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Context context,Uri uri) {
		Bitmap bm = null;		
		BufferedInputStream bis = null;
		try {			
			bis = new BufferedInputStream(context.getContentResolver()
					.openInputStream(uri), 16384);;
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return bm;
	}
}
