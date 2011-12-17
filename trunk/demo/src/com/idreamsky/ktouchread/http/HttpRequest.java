package com.idreamsky.ktouchread.http;


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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;




public abstract class HttpRequest {

	/**
	 * The supported http methods in this app.
	 */
	public static final String GET = "GET", POST = "POST";

	/**
	 * The expected response types.
	 */
	public static final int TYPE_STRING = 1, TYPE_BYTE = 2;

	private static final String TAG = "HttpRequest";

	private boolean mIsReleased = false;

	private long mContentLength = -1;

	private String mContentType;

	private HttpUriRequest mRequest;

	private int mResponseCode = -1;

	private Object mResponse;

	private String mUrl;

	public HttpRequest() {
		final String url = buildURI();
		if (Configuration.DEBUG_VERSION) {
			Log.i(TAG, "" + url);
		}
		mUrl = url;
		try {
			URI uri = new URI(url);
			HttpUriRequest request = null;

			String method = getMethod();
			if (GET.equalsIgnoreCase(method)) {
				request = new HttpGet(uri);
			} else if (POST.equalsIgnoreCase(method)) {
				request = new HttpPost(uri);
			} else {
				throw new IllegalArgumentException("execute(), method "
						+ method + " not supported.");
			}
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
		}
	}

	public void ClearData()
	{
		mResponse = null;
	}
	/**
	 * Neither {@link #GET} or {@link #POST}.
	 */
	public abstract String getMethod();

	/**
	 * The path. The SDK will automatically add the base server address prefix
	 * if necessary.
	 */
	public abstract String getPath();
	
	public  abstract boolean getIsUserCookie();

	/**
	 * For HTTP 'GET', this is the store for url parameters, for HTTP
	 * 'POST'(upload file excluded) it will be written to the http request
	 * entity.
	 */
	public abstract HashMap<String, String> getData();

	public HttpUriRequest getRequest() {
		if (mIsReleased) {
			throw new IllegalStateException("connection is already released.");
		}
		return mRequest;
	}

	public String getFinalUrl() {
		if (mIsReleased) {
			throw new IllegalStateException("connection is already released.");
		}
		return mUrl;
	}

	public int getResponseCode() {
		if (mIsReleased) {
			throw new IllegalStateException("connection is already released.");
		}
		return mResponseCode;
	}

	public Object getResponse() {
		if (mIsReleased) {
			throw new IllegalStateException("connection is already released.");
		}
		return mResponse;
	}

	public long getContentLength() {
		if (mIsReleased) {
			throw new IllegalStateException("connection is already released.");
		}
		return mContentLength;
	}

	public String getContentType() {
		if (mIsReleased) {
			throw new IllegalStateException("connection is already released.");
		}
		return mContentType;
	}

	public void releaseResources() {
		if (mIsReleased) {
			return;
		}
		mResponseCode = -1;
		mContentLength = -1;
		mContentType = null;
		mUrl = null;
		mResponse = null;

		mIsReleased = true;
	}

	/**
	 * Neither {@link #TYPE_STRING} or {@link #TYPE_BYTE}.
	 * <p>
	 * Default is {@link #TYPE_STRING}.
	 */
	protected int getExpectedType() {
		return TYPE_STRING;
	}

	public final void excute() throws ClientProtocolException, IOException,
			URISyntaxException {
		if (null == mRequest) {
			throw new URISyntaxException(getFinalUrl(), "");
		}
		DefaultHttpClient client = new DefaultHttpClient();
		//client.getCookieStore().getCookies().add(object);
		

		
		
		HttpResponse httpResponse = client.execute(mRequest);
//		if(getIsUserCookie())
//		{
//			LogEx.Log_V("Token", "Get Token From Server " +"TokenTPL" + UrlUtil.TokenTPL);
//			List<Cookie> cookies = client.getCookieStore().getCookies();
//			for(int i = 0 ; i < cookies.size(); i++)
//			{
//				if(cookies.get(i).getName().compareTo("tpl") == 0)
//				{
//					UrlUtil.TokenTPL = cookies.get(i).getValue();
//					
//					LogEx.Log_V("Token", "Get Token From Server " +"TokenTPL" + UrlUtil.TokenTPL);
//					break;
//				}
//			}			
//		}


		StatusLine sl = httpResponse.getStatusLine();
		if (null != sl) {
			mResponseCode = sl.getStatusCode();
		}

		
		HttpEntity entity = httpResponse.getEntity();

		if (null != entity) {
			mContentLength = entity.getContentLength();
			Header typeHeader = entity.getContentType();
			if (null != typeHeader) {
				mContentType = typeHeader.getValue();
			}

			final int resType = getExpectedType();
			if (TYPE_STRING == resType) {
				mResponse = EntityUtils.toString(entity, "utf-8");
			} else if (TYPE_BYTE == resType) {
				mResponse = EntityUtils.toByteArray(entity);
			} else {
				throw new IllegalArgumentException(
						"execute(), unknown response type " + resType);
			}
		} else {
			if (Configuration.DEBUG_VERSION) {
				Log.e(TAG, "the response entity is null.");
			}
		}
	}

	/**
	 * Parse a HashMap to List.
	 * 
	 * @return An empty list will returned if params is null or has no key-value
	 *         pair.
	 */
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

	private String buildURI() {
		StringBuilder urlBuilder = new StringBuilder();
		final String p = getPath();
		if (!p.startsWith("http://")) {
			//urlBuilder.append(DGCInternal.getInstance().getServerUrl());
			urlBuilder.append(p);
		} else {
			urlBuilder.append(p);
		}
		if (GET.equalsIgnoreCase(getMethod())) {
			List<NameValuePair> params = toList(getData());
			if (params.size() > 0) {
				urlBuilder.append("?");
				urlBuilder.append(URLEncodedUtils.format(params, "utf-8"));
			}
		}
		LogEx.Log_V("RequestURL", urlBuilder.toString());
		return urlBuilder.toString();
	}
	
	public String getFileExtensions(){
//		Log.d("jason", "contentType:"+contentType);
		String name = "";
		if(mContentType == null || "".equals(mContentType)){
			return "tmp";
		}
		if(mContentType.indexOf("text/html") >= 0){
			name = "txt";
        }else if(mContentType.indexOf("textnd.wap.wml") >= 0){
        	name = "textnd.wap.wml";
        }else if(mContentType.indexOf("application/xhtml+xml") >= 0){
        	name = "xml";
        }else if(mContentType.indexOf("image/jpeg") >= 0||mContentType.indexOf("image/jpg") >= 0){
        	name = "jpg";
        }else if(mContentType.indexOf("image/gif") >= 0){
        	name = "gif";
        }else if(mContentType.indexOf("image/png") >= 0){
        	name = "png";
        }else if(mContentType.indexOf("application/x-gzip") >= 0){//压缩过的页面数据
        	name = "zip";
        }else if(mContentType.indexOf("text/plain") >= 0){
        	name = "epub";
        }
		
		if("".equals(name)){
			name = "tmp";
		}
		
		return name;
	}

}


