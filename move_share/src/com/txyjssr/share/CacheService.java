package com.txyjssr.share;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.provider.MediaStore;

public class CacheService {
	private Map<Integer, String> mImagesPathMap = new HashMap<Integer, String>();
	private Map<Integer, Bitmap> mImagesMap = new HashMap<Integer, Bitmap>();
	private LinkedList<Integer> list = new LinkedList<Integer>();

	private final static int maxCacheCount = 40;

	private final static int distance = 20;

	private int currentAvg;

	public void initResource(final Context context) {
		Cursor c = context.getContentResolver().query(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, null,
				null, BaseColumns._ID);

		if (c == null || !c.moveToFirst())
			return;
		int index = 0;
		while (c.moveToNext()) {
			// int id = c.getInt(c.getColumnIndex(BaseColumns._ID));
			// Uri imageUri = Uri.withAppendedPath(
			// MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
			// String.valueOf(id));
			String imagePath = c.getString(c
					.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
			synchronized (mImagesPathMap) {
				mImagesPathMap.put(index, imagePath);
			}
			index++;
		}
		c.close();

		if (list.size() == 0) {
			for (int i = 0; i < maxCacheCount; i++) {
				list.add(i);
				// addImage(i);
			}
		}

		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < maxCacheCount; i++) {
					// list.add(i);
					addImage(i);
				}
			}
		};

		new Thread(r).start();
	}

	public void updateResourceBitmap(final int postion) {

		Runnable r = new Runnable() {
			@Override
			public void run() {

				int preFist = postion - 20;
				int preLast = postion + 20;

				int first = list.getFirst();
				int last = list.getLast();

				if (preFist < 0) {
					preFist = 0;
				}

				if (preLast >= size()) {
					preLast = size() - 1;
				}

				if (preLast < last) {
					for (preLast += 1; preLast <= preFist; preLast++) {
						mImagesMap.remove(preLast);
					}
				} else {
					for (last += 1; last <= preLast; last++) {
						addImage(preFist);
					}
				}

				if (preFist > first) {
					for (; first < preFist; first++) {
						mImagesMap.remove(first);						
					}
				} else {
					for (; preFist < first; preFist++) {
						addImage(preFist);
					}
				}

				// if (postion < currentAvg) {
				// if (first - 1 < 0)
				// return;
				// list.removeLast();
				// list.add(0, first - 1);
				// mImagesMap.remove(last);
				// addImage(first - 1);
				// }
				// if (postion > currentAvg) {
				// if (last + 1 >= mImagesPathMap.size())
				// return;
				// list.remove(0);
				// list.add(postion);
				// mImagesMap.remove(first);
				// addImage(last + 1);
				// }

			}
		};

		new Thread(r).start();

	}

	private void addImage(int position) {
		String path = mImagesPathMap.get(position);
		Bitmap bitmap = getBitmapFromPath(path);
		Bitmap des = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
		bitmap.recycle();
		bitmap = null;
		mImagesMap.put(position, des);

	}

	// private int getAvg() {
	// int sum = 0;
	// for (int i = 0; i < maxCacheCount; i++) {
	// sum += list.get(i);
	// }
	// int result = sum / maxCacheCount;
	// return result;
	// }

	private Bitmap getBitmapFromPath(String path) {
		Bitmap bm = null;
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			bm = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return bm;
	}

	public Bitmap getBitamp(int position) {
		return mImagesMap.get(position);
	}

	public int size() {
		return mImagesPathMap.size();
	}
}
