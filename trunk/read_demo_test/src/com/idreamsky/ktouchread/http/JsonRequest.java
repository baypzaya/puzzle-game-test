package com.idreamsky.ktouchread.http;

import java.io.IOException;

import org.json.JSONException;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.AbstractParser;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;
import com.idreamsky.ktouchread.xmlparase.PriceParase;

import android.util.Log;

public abstract class JsonRequest extends BaseRequest {

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
			AbstractParser parsererror = ParserFactory.createParser(
					ParserFactory.TYPE_ERROR, content);
			try {
				ErrorResult errorResult = (ErrorResult) parsererror.parse();
				
				LogEx.Log_V("APIMSG", "ResultCode:" + Integer.toString(errorResult.ResultCode) +" ResultMsg:" + errorResult.ResultMsg);
				
				if(errorResult.ResultCode == UrlUtil.Success )
				{
					if(getParserType() == ParserFactory.TYPE_ERROR)
					{
						onSuccess(errorResult.ResultMsg);
					}
					else {
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
					}

				}
				else {
					if(errorResult.ResultCode == UrlUtil.Order_relation_chapter_not_exist || errorResult.ResultCode == UrlUtil.Order_relation_book_not_exist)
					{
						new PriceParase(content).parse();
					}
					onFail(errorResult);
					onFail("服务器错误");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			// For other response code, we simply mark the error code.
			onFail(FAIL_MSG_PARSE_SERVER_ERROR);
		}
		ClearData();
	}

	public abstract void onSuccess(Object obj);

	public abstract int getParserType();

}