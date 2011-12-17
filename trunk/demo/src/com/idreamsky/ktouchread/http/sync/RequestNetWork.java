package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public abstract class RequestNetWork {

	public static int DATA_TYPE_STRING = 1;
	public static int DATA_TYPE_BYTE = 2;
	
	private HttpUriRequest mRequest;
	public String mUrl;
	
	private long mContentLength = -1;

	private String mContentType;
	private int mResponseCode = -1;
	private String mRespose = null;
	private byte[] mByteResponse = null;
	

	protected abstract String getPath();
	protected abstract int getDateType();

	protected abstract HashMap<String, String> GetParams();

	protected abstract boolean getIsUserCookie();
	
	public String GetResponse()
	{
		return mRespose;
	}
	public byte[] GetResponseByte()
	{
		return mByteResponse;
	}
	public int GetResponseCode()
	{
		return mResponseCode;
	}
	public String GetContentType()
	{
		return mContentType;
	}
	public long GetContentLength()
	{
		return mContentLength;
	}
	public void ClearData()
	{
		mRespose = null;
		mByteResponse = null;
	}
	public void Init()
	{
		final String url = buildURI();
		mUrl = url;
		LogEx.Log_V("RequestURL", mUrl);
		URI uri;
		try {
			uri = new URI(url);
			HttpUriRequest request = null;
			request = new HttpGet(uri);

			mRequest = request;

			if(getIsUserCookie())
			{
				String cookie = " ";
				//cookie = "tpl=" + "IAAAAA6SL1yTFc4IACgBLm0EG8Tj6S7Zla514qVBci8JzBfDXx/Yp7D0LhRJ/CAjcHObv1E3";
				if(UrlUtil.TokenTPL != null  && UrlUtil.TokenTPL.length() > 0 )
				{
					cookie = "tpl=" + UrlUtil.TokenTPL;
					LogEx.Log_V("Token", "Need Token " +"TokenTPL:" + UrlUtil.TokenTPL);
					
				}
				else if(UrlUtil.TokenT != null && UrlUtil.TokenT.length() > 0){
					cookie = "t=" + UrlUtil.TokenT;
					LogEx.Log_V("Token", "Need Token " +"TokenT:" + UrlUtil.TokenT);
				}
				
				mRequest.addHeader("cookie", cookie);
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public final void excute() throws ClientProtocolException, IOException,
			URISyntaxException {
		if (null == mRequest) {
			throw new URISyntaxException(mUrl, "");
		}
		DefaultHttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse = client.execute(mRequest);

		StatusLine sl = httpResponse.getStatusLine();
		if (null != sl) {
			mResponseCode = sl.getStatusCode();
		}

		HttpEntity entity = httpResponse.getEntity();

		if (null != entity) 
		{
			
			mContentLength = entity.getContentLength();
			Header typeHeader = entity.getContentType();
			if (null != typeHeader) 
			{
				mContentType = typeHeader.getValue();
			}

	
			if(getDateType() == DATA_TYPE_STRING)
			{
				
				mRespose = EntityUtils.toString(entity, "utf-8");
			}
			else if(getDateType() == DATA_TYPE_BYTE ){
				mByteResponse = EntityUtils.toByteArray(entity);
//				if(entity.getContentLength() < 400*1024)
//				{
//					mByteResponse = EntityUtils.toByteArray(entity);
//				}else {
//					mByteResponse = null;
//				}
				
			}
			
			
			
		} 
		else {
			if (Configuration.DEBUG_VERSION) {
				Log.e("MSG", "the response entity is null.");
			}
		}
	}

	private String buildURI() {
		StringBuilder urlBuilder = new StringBuilder();
		final String p = getPath();
		if (!p.startsWith("http://")) {
			// urlBuilder.append(DGCInternal.getInstance().getServerUrl());
			urlBuilder.append(p);
		} else {
			urlBuilder.append(p);
		}

		List<NameValuePair> params = toList(GetParams());
		if (params.size() > 0) {
			urlBuilder.append("?");
			urlBuilder.append(URLEncodedUtils.format(params, "utf-8"));
		}

		LogEx.Log_V("RequestURL", urlBuilder.toString());
		return urlBuilder.toString();
	}



	protected final List<NameValuePair> toList(HashMap<String, String> params) {
		if (null == params) {
			return new ArrayList<NameValuePair>();
		}
		final int size = params.size();
		if (size <= 0) {
			return new ArrayList<NameValuePair>();
		}

		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>(size);
		Set<String> keyset = params.keySet();
		for (String key : keyset) {
			String value = params.get(key);
			list.add(new BasicNameValuePair(key, value));
		}

		return list;
	}
}
