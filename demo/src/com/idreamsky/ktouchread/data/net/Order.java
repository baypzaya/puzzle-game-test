package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class Order {
	
	public static class OrderInfor
	{
		public String Msg;
		public int ResultCode;
		public String balance;
	}
	public static interface GetChapterOrderCallback extends RequestCallback {
		public void onOrderSuccess(OrderInfor balance);
		public void onFail(ErrorResult result);
	}
	
	/*
	名称	       类型 	     是否必填	     描述
	cpcode	   string	 是	          cp
	rpid	   string	 是	          cp的cp
	bookid	   string	 是	                          书id
	chapterids string	 否	          章节集合，不传则返回所有章节。
	uid	       string	 是	          用户编号，cookie中获取
	*/

	public static void GetChapterOrder(final String cpcode,final String rpid,final String bookid,final String chapterids,final String price,final GetChapterOrderCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_CHAPTER_ORDER;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_ORDER;
			}

			@Override
			public void onFail(String msg) {
				if (null != cb) {
					cb.onFail(msg);
				}
			}
			@Override
			public void onFail(ErrorResult result)
		    {
		    	cb.onFail(result);
		    }
			@Override
			public void onSuccess(Object object) {
				if (null != cb) {
					cb.onOrderSuccess((OrderInfor) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
				String productId = cpcode + "_" + rpid +"_"+bookid+"_" + chapterids;  
				paramsHashMap.put("productId", productId);
				paramsHashMap.put("productline", "2");
				paramsHashMap.put("price", price);				
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				return paramsHashMap;
			}
			@Override
			public  boolean getIsUserCookie()
			{
				return true;
			}
		}.makeRequest();
		
	}

	




}
