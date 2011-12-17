package com.idreamsky.ktouchread.http.bitmapasync;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Stack;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;

import com.idreamsky.ktouchread.http.BitmapRequest;
import com.idreamsky.ktouchread.http.sync.RequestBitmap;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.SDCardUtils;


public class AsyncImageLoader {
	
	private String SyncTag = "DownLaodBitmp";
//	private List<DownloadImage> mMapDownload = new ArrayList<AsyncImageLoader.DownloadImage>();
	private Stack<DownloadImage> mStackDownload = new Stack<AsyncImageLoader.DownloadImage>();
	private DownloadImage mDownloadImage = null;
	private HashMap<String, String>  mKeyMap = new HashMap<String, String>();
	public class DownloadImage
	{
		public  String URL;
		public String bookID;
		public ImageCallback mImageCallback;
	}

	public AsyncImageLoader() {
	}

	final Handler handler = new Handler() {
		public void handleMessage(Message message) {
			
			switch(message.what)
			{
			  case 0:
			  {
				  if(mDownloadImage == null )
				  {
					  mDownloadImage =  Get();
					  if(mDownloadImage != null)
					  {
//						  if(mKeyMapDelete.containsKey(mDownloadImage.bookID))
//						  {
//							  mKeyMapDelete.remove(mDownloadImage.bookID);
//							  if(mKeyMap.containsKey(mDownloadImage.bookID))
//							        mKeyMap.remove(mDownloadImage.bookID);
//							  mDownloadImage =null;
//							  handler.sendEmptyMessage(0);
//						  }
//						  else
//						  {
//							  DownloadBitmap(mDownloadImage);
//							  LogEx.Log_V("Bitmap", "1:" + mDownloadImage.bookID);
//						  }
						  
						  DownloadBitmap(mDownloadImage);
						  LogEx.Log_V("Bitmap", "1:" + mDownloadImage.bookID);
					  }
				  }
				  else {
					
					  LogEx.Log_V("Bitmap", "2:" + mDownloadImage.bookID);
				}

			  }
			  break;
			  case 1:
			  {
				  Bitmap drawable = (Bitmap)message.obj;
				  if(mDownloadImage != null)
				  {
					  LogEx.Log_V("Bitmap", "3:" + mDownloadImage.bookID);
					  if(mKeyMap.containsKey(mDownloadImage.bookID))
					        mKeyMap.remove(mDownloadImage.bookID);
					  
					  mDownloadImage.mImageCallback.imageLoaded(drawable,mDownloadImage.bookID);
					  mDownloadImage = null;
				  }
				  System.gc();
				  handler.sendEmptyMessage(0);
				  
			  }
			  break;
			  case 2:
			  {
				
				  if(mDownloadImage != null)
				  {
					  LogEx.Log_V("Bitmap", "3:" + mDownloadImage.bookID);
					  if(mKeyMap.containsKey(mDownloadImage.bookID))
					        mKeyMap.remove(mDownloadImage.bookID);
					  mDownloadImage = null;
				  }
				  System.gc();
				  handler.sendEmptyMessage(0);
				  
			  }
			  break;
			}

		}
	};
	public Bitmap loadDrawable(final String imageUrl,final String BookIDNet,
			final ImageCallback imageCallback) {
		if (ImgUtil.sBitmapPool.containsKey(BookIDNet)) {
			WeakReference<Bitmap> softReference = ImgUtil.sBitmapPool.get(BookIDNet);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				if(drawable.isRecycled())
				{
					return drawable;
				}
				else {
					ImgUtil.sBitmapPool.remove(BookIDNet);
				}
				
			}
		}
		
		DownloadImage downloadImage = new DownloadImage();
		downloadImage.bookID = BookIDNet;
		downloadImage.URL = imageUrl;
		downloadImage.mImageCallback = imageCallback;
		//mMapDownload.add(downloadImage);
		LogEx.Log_V("Bitmap", "4:" + downloadImage.bookID);
		if(Add(downloadImage) && mDownloadImage == null)
			handler.sendEmptyMessage(0);
		return null;
	}

	private boolean Add(DownloadImage downloadImage)
	{
		synchronized(SyncTag){
			if(mKeyMap.containsKey(downloadImage.bookID))
				return false;
			mStackDownload.push(downloadImage);
			mKeyMap.put(downloadImage.bookID, downloadImage.bookID);
			return true;
		}
	}
	private DownloadImage Get()
	{
		synchronized(SyncTag){
		
			if(mStackDownload.size() >0)
			{
				return mStackDownload.pop();
			}
			else {
				return null;
			}
		}
	}
	private void DownloadBitmap(final DownloadImage item)
	{
		new Thread() {
			@Override
			public void run() {
				Bitmap drawable = loadImageFromUrl(item.URL);
				LogEx.Log_V("Bitmap", "5:" + item.bookID);
				if(drawable != null)
				{
					
					if(drawable != null && SDCardUtils.isSDCardEnable())
					{

						ImgUtil.SaveBitmap(drawable,"jpg",item.bookID);
						
					}
					Message message = handler.obtainMessage(1, drawable);
					handler.sendMessage(message);
				}
				else {
					handler.sendEmptyMessage(2);
				}
			}
		}.start();
	}
	public static Bitmap loadImageFromUrl(String url) {

		if(!checkUrl(url))
		{
			return null;
		}
		Bitmap bitmap = new RequestBitmap(url).GetBitmap();
		Bitmap bitmapnewBitmap = compressIfNecessary(bitmap);


		return bitmapnewBitmap;
	}
	
	private static  boolean checkUrl(String URL)
	{
		String url = URL.toLowerCase();
		if(url.endsWith("png") || url.endsWith("bmp") || url.endsWith("jpg") || url.endsWith("gif"))
		{
			return true;
		}
		return false;
	}

	protected final static Bitmap compressIfNecessary(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (w <= BitmapRequest.BMP_MAX_WIDTH && h <= BitmapRequest.BMP_MAX_HEIGHT) {
			return bitmap;
		}

		float scale = w > h ? BitmapRequest.BMP_MAX_WIDTH / (float) w : BitmapRequest.BMP_MAX_HEIGHT
				/ (float) h;



		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
		bitmap.recycle();
		bitmap = null;
		return b;
	}
	
	public interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, String bookid);
	}

}
