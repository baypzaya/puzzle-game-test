package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import android.util.Log;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class KTouchToken {
	
	public String ktouchToken;
	
	public interface KTouchTokenCallback {
		public void onSuccess(Object o);

		public void onFaile(String msg);
	}

	public static void getToken(final String token, final String secret,final KTouchTokenCallback cb) {
		new JsonRequest() {

			@Override
			public void onSuccess(Object obj) {
				cb.onSuccess(obj);
			}

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_KTOUCHTOKEN;
			}

			@Override
			public void onFail(String msg) {
				cb.onFaile(msg);

			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_KTOUCH_TOKEN;
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("token", token);
				paramsHashMap.put("secret", secret);
				paramsHashMap.put("imsi", UrlUtil.imsi);
				paramsHashMap.put("app", "2");

				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return true;
			}

		}.makeRequest();
		
	};
	
	
}
