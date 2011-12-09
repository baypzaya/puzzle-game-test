package com.idreamsky.ktouchread.http;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.EntityTemplate;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.ReadInternal;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;

import android.os.Handler;
import android.os.Looper;



public abstract class BaseRequest extends HttpRequest implements HttpResponse {

	public static final String FAIL_MSG_PARSE_SERVER_ERROR = "服务器错误";
	public static final String FAIL_MSG_PARSE_JSON = "数据解析错误";
	public static final String FAIL_MSG_HTTP_IO = "网络错误";
	public static final String FAIL_MSG_TIMEOUT = "网络超时";

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HashMap<String, String> getData() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final int DEFAULT_REQUEST_TIMEOUT = 20000;

	/*
	 * FIXME If the developer construct this object in non-main thread, this
	 * statement will throw an exception.
	 */
	// protected final Handler mHandler = new Handler();

	/*
	 * Make sure this handler is attached in the main thread.
	 */
	protected static final Handler sHandler = new Handler(
			Looper.getMainLooper());

	//private OAuthUtils mUtils;

	private boolean mIsRequestMade = false;

	/*
	 * Declare canceled field as 'volatile' to keep up its visibility in all
	 * related threads.
	 */
	private volatile boolean canceled = false;

	public BaseRequest() {
		super();
	}

	protected void markCanceled() {
		canceled = true;
	}

	protected boolean isCanceled() {
		return canceled;
	}

	protected Runnable mTimeoutRunnable = new Runnable() {

		@Override
		public void run() {
			markCanceled();
			onTimeout();
		}
	};

//	private void addOAuthHeader() {
//		String consumerKey = DGCInternal.getInstance().getConsumerKey();
//		String consumerSecret = DGCInternal.getInstance().getConsumerSecret();
//		String tokenSecret = DGCInternal.getInstance().getAccessTokenSecret();
//		String signatureMethod = OAuthUtils.SIGNATURE_METHOD;
//		String accessToken = DGCInternal.getInstance().getAccessToken();
//
//		String timeStamp = mUtils.generateTimestamp();
//		String nonce = mUtils.generateNonce();
//		String version = OAuthUtils.VERSION_1_0;
//
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put(OAuthUtils.OAUTH_CONSUMER_KEY, consumerKey);
//		params.put(OAuthUtils.OAUTH_TOKEN, accessToken);
//		params.put(OAuthUtils.OAUTH_SIGNATURE_METHOD, signatureMethod);
//		params.put(OAuthUtils.OAUTH_TIMESTAMP, timeStamp);
//		params.put(OAuthUtils.OAUTH_NONCE, nonce);
//		params.put(OAuthUtils.OAUTH_VERSION, version);
//		try {
//			String signature = mUtils.sign(getMethod(), getFinalUrl(), params,
//					consumerSecret, tokenSecret);
//			String[] kvs = new String[] { OAuthUtils.OAUTH_CONSUMER_KEY,
//					consumerKey, OAuthUtils.OAUTH_TOKEN, accessToken,
//					OAuthUtils.OAUTH_SIGNATURE_METHOD, signatureMethod,
//					OAuthUtils.OAUTH_SIGNATURE, signature,
//					OAuthUtils.OAUTH_TIMESTAMP, timeStamp,
//					OAuthUtils.OAUTH_NONCE, nonce, OAuthUtils.OAUTH_VERSION,
//					version };
//			String header = OAuthUtils.prepareOAuthHeader(kvs);
//			getRequest()
//					.addHeader(OAuthUtils.HTTP_AUTHORIZATION_HEADER, header);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Determine whether this request needs OAuth verifying.
	 * <p>
	 * Return false to disable.
	 */
	protected boolean needOAuth() {
		return true;
	}

	/**
	 * Determine whether parse the response to a json object.
	 * <p>
	 * Return false to disable.
	 */
	protected boolean parseJsonRequired() {
		return true;
	}

	/**
	 * Sets the value of the specified request header field.
	 * <p>
	 * Must be called before {@link #makeRequest()}.
	 */
	public final void setRequestHeader(String key, String value) {
		if (mIsRequestMade) {
			throw new IllegalStateException("Must called before makeRequest()");
		}
		getRequest().setHeader(key, value);
	}

	/**
	 * Adds the given property to the request header. The header will be
	 * appended to the end of the list.
	 * <p>
	 * Must be called before {@link #makeRequest()}.
	 */
	public final void addRequestHeader(String key, String value) {
		if (mIsRequestMade) {
			throw new IllegalStateException("Must called before makeRequest()");
		}
		getRequest().addHeader(key, value);
	}

	public final void makeRequest() {
		mIsRequestMade = true;
		/*
		 * This timeout runnable must be enqueued to the message queue before
		 * executing the request runnable.
		 * 
		 * Make sure when the http response ends the time runnable is already
		 * there.
		 */
		sHandler.postDelayed(mTimeoutRunnable, getDefaultTimeout());
		ThreadPoolExecutor tpe = ReadInternal.getInstance().getPoolExecutor();
		if (null == tpe) {
			/*
			 * Well the ThreadPoolExecutor has been shutdown and points to null
			 * here, so can not execute any Runnable.
			 */
			return;
		}
		tpe.execute(new Runnable() {

			@Override
			public void run() {
//				if (DGCInternal.getInstance().isAccessTokenReady()
//						&& needOAuth()) {
//					mUtils = new OAuthUtils();
//					addOAuthHeader();
//				}
				if (HttpRequest.POST.equalsIgnoreCase(getMethod())) {
					HttpPost post = (HttpPost) getRequest();
					String type = getMIMEType();
					byte[] uploadData = getUploadData();

					if (null == type || null == uploadData) {
						List<NameValuePair> params = toList(getData());
						if (params.size() > 0) {
							UrlEncodedFormEntity entity;
							try {
								entity = new UrlEncodedFormEntity(params,
										"utf-8");
								post.setEntity(entity);
							} catch (UnsupportedEncodingException e) {
							}

						}
					} else {
						/*
						 * Well, it is a upload file request, so translate the
						 * entity.
						 */
						post.setHeader("Connection", "Keep-Alive");
						post.setHeader("Content-Type",
								"multipart/form-data; boundary="
										+ MultiProducer.BOUNDARY);
						HttpEntity entity = new EntityTemplate(
								new MultiProducer(uploadData, type, getData()));
						post.setEntity(entity);
					}
				}
				try {
					excute();
					onStateChanged(BaseRequest.this, HttpResponse.HTTP_COMPLETE);
				} catch (Exception e) {
					if (Configuration.DEBUG_VERSION) {
						e.printStackTrace();
					}
					onStateChanged(BaseRequest.this, HttpResponse.HTTP_FAILED);
				}

			}
		});
	}

	@Override
	public void onStateChanged(final HttpRequest request, final int state) {
		final Handler handler = sHandler;
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (isCanceled()) {
					return;
				}
				if (HttpResponse.HTTP_COMPLETE == state) {
					onHttpComplete(request);
					request.releaseResources();
				} else if (HttpResponse.HTTP_FAILED == state) {
					onHttpFail(request);
					request.releaseResources();
				}

				handler.removeCallbacks(mTimeoutRunnable);
			}
		});
	}

	/**
	 * The MIME type of the file. Simply returns null here.
	 * <p>
	 * Used to upload file.
	 */
	public String getMIMEType() {
		return null;
	}

	/**
	 * The raw data of the file. Simply returns null here.
	 * <p>
	 * Used to upload file.
	 */
	public byte[] getUploadData() {
		return null;
	}

	protected int getDefaultTimeout() {
		return DEFAULT_REQUEST_TIMEOUT;
	}

	/**
	 * Called when the request timeouts. Override {@link #getDefaultTimeout()}
	 * to change.
	 * <p>
	 * <b>Runs in UI thread.
	 */
	protected void onTimeout() {
		//onFail("Timeout to connect to " + getPath());
		onFail("网络超时！");
	}

	/**
	 * Called when the http request failed.
	 * <p>
	 * <b>Runs in UI thread.
	 */
	protected void onHttpFail(HttpRequest request) {
		//onFail("HTTP request IO error");
		ErrorResult errorResult = new ErrorResult();
		errorResult.ResultMsg = FAIL_MSG_TIMEOUT;
		errorResult.ResultCode = UrlUtil.ServerTimeOut ;
		onFail(FAIL_MSG_TIMEOUT);
		onFail(errorResult);
	}

	/**
	 * Called when the response has ended.
	 * <p>
	 * <b>Runs in UI thread.
	 */
	protected abstract void onHttpComplete(HttpRequest request);

	public abstract void onFail(String msg);
    protected void onFail(ErrorResult result)
    {
    	
    }

}


