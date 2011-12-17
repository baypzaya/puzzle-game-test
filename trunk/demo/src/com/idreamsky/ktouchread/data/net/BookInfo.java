package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class BookInfo {
	
	public String cpcode;
	public String rpid;
	public String bookid;
	public String bookname;
	public String authorid;
	public String authorname;
	public String description;
	public String coverimageurl;
	public int billingtype;       //1：整本2：章节3：免费
	public int bookstatus;        // 1连载中 0完结
	public String updatets;
	public String CategoryName;
	
	public String categoryid;
	public String price;
	public int chaptercount;
	public String LastUpdateChapterId;
	public String LastUpdateChapterName;
	public String LastUpdateChapterTs;
	public String Updatets;
	
	
	public static interface GetBookInforCallback extends RequestCallback {
		public void onSuccess(BookInfo bookinfo);
	}
	
	//http://hostname/book/getbasicinfo? cpcode=yzsc&bookid=1719982&rpid=10
/*
	名称  	类型	    是否必填 	描述
	cpcode	string	是	        cp
	rpid	string	是	        cp的cp
	bookid	string	是	        书id
*/
	public static void GetBookInfo(final String cpcode ,final String rpid,final String bookid ,final GetBookInforCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_BOOKINFOR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_BOOK_INFO;
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
					cb.onSuccess((BookInfo) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
				paramsHashMap.put("cpcode", cpcode);
				paramsHashMap.put("rpid", rpid);
				paramsHashMap.put("bookid", bookid);				
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
