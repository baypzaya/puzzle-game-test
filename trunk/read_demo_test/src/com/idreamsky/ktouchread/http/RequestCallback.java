package com.idreamsky.ktouchread.http;

public interface RequestCallback {

	/**
	 * Invoked when the http request fails.
	 * 
	 * @param msg
	 *            the fail message.
	 */
	public void onFail(String msg);
}