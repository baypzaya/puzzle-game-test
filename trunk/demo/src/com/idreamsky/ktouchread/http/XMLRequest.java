package com.idreamsky.ktouchread.http;



import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.xmlparase.AbstractParser;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

import android.util.Log;

public abstract class XMLRequest extends BaseRequest {

	@Override
	public String getMethod() {
		return HttpRequest.GET;
	}

	@Override
	protected void onHttpComplete(HttpRequest request) {
		final int code = request.getResponseCode();
		String content = (String) getResponse();
		if (code >= 400 && code <= 499) {
			/*
			 * For 4xx response code, the server will set the error message.
			 */
			AbstractParser parser = ParserFactory.createParser(
					ParserFactory.TYPE_ERROR, content);
			String error = null;
			try {
				error = (String) parser.parse();
			} catch (Exception e) {
			}
			if (null != error) {
				onFail(error);
			} else {
				onFail(FAIL_MSG_PARSE_SERVER_ERROR);
			}
		} else if (code == 200) {
			AbstractParser parser = ParserFactory.createParser(getParserType(),
					content);
			Object object = null;
			try {
				object = parser.parse();
			} catch (Exception e) {
				if (Configuration.DEBUG_VERSION) {
					Log.e(Configuration.TAG, "url: " + getFinalUrl());
					Log.e(Configuration.TAG, "response: " + content);
					e.printStackTrace();
				}
			}
			if (null != object) {
				onSuccess(object);
			} else {
				onFail(FAIL_MSG_PARSE_JSON);
			}
		} else {
			// For other response code, we simply mark the error code.
			onFail("Server responsed with code " + code);
		}
	}

	public abstract void onSuccess(Object obj);

	public abstract int getParserType();

}