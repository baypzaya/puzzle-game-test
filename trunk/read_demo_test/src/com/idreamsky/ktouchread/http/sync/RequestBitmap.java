package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RequestBitmap extends RequestNetWork{
	private String URL = null;
	public static int DownLoadCount = 0;
	public RequestBitmap(String url)
	{
		URL = url;
		try {
			Init();
		} catch (Exception e) {
			// TODO: handle exception
		}
		RequestBitmap.DownLoadCount++;
		//Log.v("Bitmap", "RequestBitmap.DownLoadCount++"+RequestBitmap.DownLoadCount);
		
	}

	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return URL;
	}

	@Override
	protected int getDateType() {
		// TODO Auto-generated method stub
		return DATA_TYPE_BYTE;
	}

	@Override
	protected HashMap<String, String> GetParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	public Bitmap GetBitmap()
	{
		try {
			this.excute();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		final int code = GetResponseCode();
		if(code == 200)
		{
			byte[] buffer = GetResponseByte();
			if(buffer != null && buffer.length > 0)
			{
				
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
//				BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opts);

				opts.inSampleSize = 3;// ImgUtil.computeSampleSize(opts, -1, BitmapRequest.BMP_MAX_WIDTH*BitmapRequest.BMP_MAX_HEIGHT);
				opts.inJustDecodeBounds = false;
				try {
					Bitmap bmp =  BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opts);
					buffer = null;
					ClearData();
					return bmp;
				    } catch (OutOfMemoryError err) {
				}finally{
					if(RequestBitmap.DownLoadCount > 0)
					{			
						RequestBitmap.DownLoadCount--;	
						//LogEx.Log_V("Bitmap", "RequestBitmap.DownLoadCount--"+RequestBitmap.DownLoadCount);
						//Log.v("Bitmap", "RequestBitmap.DownLoadCount--"+RequestBitmap.DownLoadCount);
					}
				}
				    
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				opts.inSampleSize = 4;
//				return BitmapFactory.decodeByteArray(buffer, 0, buffer.length,opts);
			}
		}
		return null;

	}
}
