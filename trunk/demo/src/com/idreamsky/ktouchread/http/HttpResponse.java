package com.idreamsky.ktouchread.http;
public interface HttpResponse {

	/**
	 * The request succeeds and receiving response message has completed.
	 */
	public static final int HTTP_COMPLETE = 12;

	/**
	 * The request has fails.
	 */
	public static final int HTTP_FAILED = 13;

	/**
	 * Called when state changes.
	 * 
	 * @param request
	 * @param state
	 */
	public void onStateChanged(HttpRequest request, int state);

}
