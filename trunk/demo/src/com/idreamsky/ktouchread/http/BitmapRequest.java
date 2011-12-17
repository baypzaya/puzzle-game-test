package com.idreamsky.ktouchread.http;


import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.SDCardUtils;


public abstract class BitmapRequest extends BaseRequest {

	public static final int BMP_MAX_WIDTH = 86;
	public static final int BMP_MAX_HEIGHT = 116;

	private static final String TAG = "BitmapRequest";

	@Override
	public String getMethod() {
		return HttpRequest.GET;
	}

	@Override
	public int getExpectedType() {
		return TYPE_BYTE;
	}

	@Override
	protected void onHttpComplete(final HttpRequest request) {
		final byte[] response = (byte[]) request.getResponse();
		Bitmap bitmap = BitmapFactory.decodeByteArray(response, 0,
				response.length);
		final Bitmap compressedBitmap = compressRequired() ? compressIfNecessary(bitmap)
				: bitmap;
		sHandler.post(new Runnable() {

			@Override
			public void run() {
				if (null != compressedBitmap) {
					onSuccess(compressedBitmap);
				} else {
					onFail("Can not decode " + getPath() + " as a bitmap.");
				}
				request.releaseResources();
			}
		});
	}

	@Override
	public void onStateChanged(final HttpRequest request, final int state) {
		if (isCanceled()) {
			return;
		}
		final Handler handler = sHandler;

		if (HttpResponse.HTTP_COMPLETE == state) {
			onHttpComplete(request);
		} else if (HttpResponse.HTTP_FAILED == state) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					onHttpFail(request);
					request.releaseResources();
				}
			});
		}
		handler.removeCallbacks(mTimeoutRunnable);
	}

	public abstract void onSuccess(Bitmap bitmap);

	protected boolean compressRequired() {

		return false;
	}

	protected final Bitmap compressIfNecessary(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (w <= BMP_MAX_WIDTH && h <= BMP_MAX_HEIGHT) {
			return bitmap;
		}

		float scale = w > h ? BMP_MAX_WIDTH / (float) w : BMP_MAX_HEIGHT
				/ (float) h;

		if (Configuration.DEBUG_VERSION) {
			Log.v(TAG, "Compress bitmap, scale is " + scale);
		}

		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
		if (Configuration.DEBUG_VERSION) {
			Log.v(TAG, "Compress bitmap to size w: " + b.getWidth() + ", h: "
					+ b.getHeight());
		}
		return b;
	}

	@Override
	protected boolean needOAuth() {
		return false;
	}

	private static ConcurrentHashMap<String, SoftReference<Bitmap>> sBitmapPool;

	private static HashMap<String, LinkedList<WeakReference<ImageView>>> sImagePool;

	private static HashSet<String> sRequestSet;

	public static final void removeBitmapFromPool(String key) {
		if (sBitmapPool.containsKey(key)) {
			sBitmapPool.remove(key);
		}
	}

	static {
		sBitmapPool = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
		sImagePool = new HashMap<String, LinkedList<WeakReference<ImageView>>>();
		sRequestSet = new HashSet<String>();
	}

	private static final byte[] SYNC = new byte[0];

	private static final void fillImageView(final String url,
			final ImageView iv, final Drawable defaultIcon,
			final Drawable background,final String BookIDNet) {
		if (null == url || !url.startsWith("http://")) {
			if (null == defaultIcon && null == background) {
				iv.setVisibility(View.GONE);
			}
			return ;
		}
		final SoftReference<Bitmap> bitmapRef = sBitmapPool.get(url);
		if (null != bitmapRef) {
			Bitmap bitmap = bitmapRef.get();
			if (null != bitmap) {
				iv.setImageBitmap(bitmap);
				return ;
			}
			// Well there is a key maps to the bitmap, but the bitmap is null,
			// which means it has been recycled by gc sometime before. So remove
			// the key and prepare to save the next bitmap to be requested.
			sBitmapPool.remove(url);
		}

		if (null == defaultIcon && null == background) {
			iv.setVisibility(View.GONE);
		}
		
		synchronized (SYNC) {
			HashSet<String> requestSet = sRequestSet;
			if (!requestSet.contains(url)) {
				requestSet.add(url);
			} else {
				HashMap<String, LinkedList<WeakReference<ImageView>>> ivPool = sImagePool;
				LinkedList<WeakReference<ImageView>> list;
				if (!ivPool.containsKey(url)) {
					list = new LinkedList<WeakReference<ImageView>>();
					ivPool.put(url, list);
				} else {
					list = ivPool.get(url);
				}
				WeakReference<ImageView> ivRef = new WeakReference<ImageView>(
						iv);
				list.add(ivRef);
				return ;
			}
		}
		

		if(SDCardUtils.isSDCardEnable())
		{
			Bitmap bitmap = ImgUtil.getImageByPath(BookIDNet);
			if(bitmap != null)
			{
				iv.setVisibility(View.VISIBLE);
				iv.setImageBitmap(bitmap);
				sBitmapPool.put(url, new SoftReference<Bitmap>(bitmap));
				return;
			}
		}




		// Start request
		new BitmapRequest() {
			@Override
			public void onSuccess(final Bitmap bitmap) {
				if (View.GONE == iv.getVisibility()) {
					iv.setVisibility(View.VISIBLE);
				}

//				if(iv.getTag() != null)
//				{
//					if(((String)iv.getTag()).compareTo(url)==0)
//					{
//						iv.setImageBitmap(bitmap);
//					}
//				}
//				else {
//					iv.setImageBitmap(bitmap);
//				}
				iv.setImageBitmap(bitmap);
				if(SDCardUtils.isSDCardEnable())
				{
					if(getFileExtensions().compareTo("png") == 0 || getFileExtensions().compareTo("jpg") == 0||getFileExtensions().compareTo("gif") == 0)
					{
						ImgUtil.SaveBitmap(bitmap,getFileExtensions(),BookIDNet);
					}
				}


				// Put the bitmap whenever a bitmap request succeeds.
				sBitmapPool.put(url, new SoftReference<Bitmap>(bitmap));

				HashMap<String, LinkedList<WeakReference<ImageView>>> ivPool = sImagePool;
				if (ivPool.containsKey(url)) {
					LinkedList<WeakReference<ImageView>> list = ivPool.get(url);
					for (WeakReference<ImageView> ivRef : list) {
						ImageView image = ivRef.get();
						if (null != image) {
							image.setImageBitmap(bitmap);
						}
					}
					ivPool.remove(url);
				}

				HashSet<String> set = sRequestSet;
				if (set.contains(url)) {
					set.remove(url);
				}
			}

			@Override
			public void onFail(String msg) {
				if (Configuration.DEBUG_VERSION) {
					Log.e(Configuration.TAG, msg);
				}
				HashMap<String, LinkedList<WeakReference<ImageView>>> ivPool = sImagePool;
				if (ivPool.containsKey(url)) {
					ivPool.remove(url);
				}

				HashSet<String> set = sRequestSet;
				if (set.contains(url)) {
					set.remove(url);
				}
			}

			@Override
			public String getPath() {
				return url;
			}

			@Override
			public HashMap<String, String> getData() {
				return null;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return false;
			}
		}.makeRequest();

		return ;
	}

	private static final void fillImageView(final String url,
			final ImageView iv, final Drawable defaultIcon,
			final Drawable background,final String BookIDNet,Object obj) {
		if (null == url || !url.startsWith("http://")) {
			if (null == defaultIcon && null == background) {
				iv.setVisibility(View.GONE);
			}
			return ;
		}
		final SoftReference<Bitmap> bitmapRef = sBitmapPool.get(url);
		if (null != bitmapRef) {
			Bitmap bitmap = bitmapRef.get();
			if (null != bitmap) {
				iv.setBackgroundDrawable(ImgUtil.drawableToBitmap(bitmap));
				return ;
			}
			// Well there is a key maps to the bitmap, but the bitmap is null,
			// which means it has been recycled by gc sometime before. So remove
			// the key and prepare to save the next bitmap to be requested.
			sBitmapPool.remove(url);
		}

		if (null == defaultIcon && null == background) {
			iv.setVisibility(View.GONE);
		}
		
		synchronized (SYNC) {
			HashSet<String> requestSet = sRequestSet;
			if (!requestSet.contains(url)) {
				requestSet.add(url);
			} else {
				HashMap<String, LinkedList<WeakReference<ImageView>>> ivPool = sImagePool;
				LinkedList<WeakReference<ImageView>> list;
				if (!ivPool.containsKey(url)) {
					list = new LinkedList<WeakReference<ImageView>>();
					ivPool.put(url, list);
				} else {
					list = ivPool.get(url);
				}
				WeakReference<ImageView> ivRef = new WeakReference<ImageView>(
						iv);
				list.add(ivRef);
				return ;
			}
		}
		

		if(SDCardUtils.isSDCardEnable())
		{
			Bitmap bitmap = ImgUtil.getImageByPath(BookIDNet);
			if(bitmap != null)
			{
				iv.setVisibility(View.VISIBLE);
				iv.setBackgroundDrawable(ImgUtil.drawableToBitmap(bitmap));
				sBitmapPool.put(url, new SoftReference<Bitmap>(bitmap));
				return;
			}
		}




		// Start request
		new BitmapRequest() {
			@Override
			public void onSuccess(final Bitmap bitmap) {
				if (View.GONE == iv.getVisibility()) {
					iv.setVisibility(View.VISIBLE);
				}

//				if(iv.getTag() != null)
//				{
//					if(((String)iv.getTag()).compareTo(url)==0)
//					{
//						iv.setImageBitmap(bitmap);
//					}
//				}
//				else {
//					iv.setImageBitmap(bitmap);
//				}
				iv.setBackgroundDrawable(ImgUtil.drawableToBitmap(bitmap));
				if(SDCardUtils.isSDCardEnable())
				{
					if(getFileExtensions().compareTo("png") == 0 || getFileExtensions().compareTo("jpg") == 0||getFileExtensions().compareTo("gif") == 0)
					{
						ImgUtil.SaveBitmap(bitmap,getFileExtensions(),BookIDNet);
					}
				}


				// Put the bitmap whenever a bitmap request succeeds.
				sBitmapPool.put(url, new SoftReference<Bitmap>(bitmap));

				HashMap<String, LinkedList<WeakReference<ImageView>>> ivPool = sImagePool;
				if (ivPool.containsKey(url)) {
					LinkedList<WeakReference<ImageView>> list = ivPool.get(url);
					for (WeakReference<ImageView> ivRef : list) {
						ImageView image = ivRef.get();
						if (null != image) {
							image.setBackgroundDrawable(ImgUtil.drawableToBitmap(bitmap));
						}
					}
					ivPool.remove(url);
				}

				HashSet<String> set = sRequestSet;
				if (set.contains(url)) {
					set.remove(url);
				}
			}

			@Override
			public void onFail(String msg) {
				if (Configuration.DEBUG_VERSION) {
					Log.e(Configuration.TAG, msg);
				}
				HashMap<String, LinkedList<WeakReference<ImageView>>> ivPool = sImagePool;
				if (ivPool.containsKey(url)) {
					ivPool.remove(url);
				}

				HashSet<String> set = sRequestSet;
				if (set.contains(url)) {
					set.remove(url);
				}
			}

			@Override
			public String getPath() {
				return url;
			}

			@Override
			public HashMap<String, String> getData() {
				return null;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return false;
			}
		}.makeRequest();

		return ;
	}


	public static final void fillImageView(final String url, final ImageView iv,String BookIDNet) {
		 fillImageView(url, iv, iv.getDrawable(), iv.getBackground(),BookIDNet);
	}
	
	public static final void fillImageView(final String url, final ImageView iv,String BookIDNet,Object obj) {
		 fillImageView(url, iv, iv.getDrawable(), iv.getBackground(),BookIDNet,obj);
	}

}


