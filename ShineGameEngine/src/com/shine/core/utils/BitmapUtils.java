package com.shine.core.utils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapUtils {

	/**
	 * 获取图片资源
	 * 
	 * @param context
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri)
			throws IOException {
		InputStream input = null;
		Bitmap bm = null;
		try {
			input = context.getContentResolver().openInputStream(uri);
			bm = getBitmapFromInput(context, input);
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return bm;
	}

	public static Bitmap getBitmapFromAsset(Context context, String fileName)
			throws IOException {
		InputStream input = null;
		Bitmap bm = null;
		try {
			input = context.getAssets().open(fileName);
			bm = getBitmapFromInput(context, input);
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return bm;
	}

	public static Bitmap getBitmapFromInput(Context context, InputStream input) {
		Bitmap bm = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(input, 16384);
			;
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
