package com.idreamsky.ktouchread.data.net;



import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;



public class SearchKey {
	public static int TotalCount = 0;
	
	public static interface GetKeyCallback extends RequestCallback {
		public void onSuccess(List<String > keyList);
		public void onUpdate(List<String > keyList);
	}

	public static void GetSearchKey(final GetKeyCallback cb)
	{
		List<String> keyList = BookDataBase.getInstance().QuerySearchKeys();
		if(keyList.size() > 0)
		{
			cb.onSuccess(keyList);
		}
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_SEARCH_KEY;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_SEARCH_KEY;
			}

			@Override
			public void onFail(String msg) {
				if (null != cb) {
					cb.onFail(msg);
				}
			}
			
			@Override
			public void onSuccess(Object object) {
				if (null != cb) {
					
					List<String> keyList = (List<String>) object;
					BookDataBase.getInstance().DeleteAllSearchKey();
					BookDataBase.getInstance().InsertSearchKey(keyList);						
					cb.onUpdate(keyList);
				}
			}

			@Override
			public HashMap<String, String> getData() {

				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("imei", UrlUtil.imei);
				paramsHashMap.put("imsi", UrlUtil.imsi);
				paramsHashMap.put("deviceIdentify", UrlUtil.deviveid);
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return true;
			}
		}.makeRequest();

	}
}
