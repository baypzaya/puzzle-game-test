package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class NetBookShelf {
	
	
	public static class BookEx
	{
		public String cpcode	;//cp		
		public String rpid	;    //cp的cp		
		public String bookid	;//书籍编号		
		public String bookname;  //	书名		
		public String coverimageurl;
		public int feeds;
		public int bookstatus;  //1连载中 0完结
		public int ischase;     //0否1是
		public int chasetype;   //0：普通1：高级
		public int updatets;
		public String authorName;
		public int isDel;
	

	}
	
	public static interface AddBookCallback extends RequestCallback {
		public void onSuccess(String strMsg);
	}
	
	//http://hostname/shelf/add? bookid=1727516&cpcode=yzsc&rpid=10
/*
	名称	          类型	         是否必填	描述
	cpcode	string	是	cp
	rpid	string	是	cp的cp
	bookid	string	是	书id
	uid	    string	是	用户编号，cookie中获取
*/
	public static void add(final String cpcode,final String rpid,final String bookid ,final AddBookCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_ERROR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOK_SHELF_ADD;
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
					cb.onSuccess((String) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("cpcode",cpcode);
				paramsHashMap.put("rpid", rpid);
				paramsHashMap.put("bookid",bookid);
				paramsHashMap.put("imei", UrlUtil.imei);
				paramsHashMap.put("imsi", UrlUtil.imsi);
				paramsHashMap.put("deviceIdentify", UrlUtil.deviveid);
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);

				return paramsHashMap;
			}
			
			@Override
		    public void onFail(ErrorResult result)
		    {
		    	
		    }
			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return true;
			}
		}.makeRequest();
	}

	public static interface DeleteBookCallback extends RequestCallback {
		public void onSuccess(String strMsg);
	}
	
	//http://hostname/shelf/delete? bookid=1727516&cpcode=yzsc&rpid=10
	/*
	名称	           类型	是否必填	描述
	cpcode	string	是	cp
	rpid	string	是	cp的cp
	bookid	string	是	书id
	uid	    string	是	用户编号，cookie中获取
    */
	public static void delete(final String cpcode,final String rpid,final String bookid,final DeleteBookCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_ERROR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOK_SHELF_DELETE;
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
		    	
		    }
			@Override
			public void onSuccess(Object object) {
				if (null != cb) {
					cb.onSuccess((String) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("cpcode",cpcode);
				paramsHashMap.put("rpid", rpid);
				paramsHashMap.put("bookid",bookid);
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
	
	public static interface GetBookShelfListCallback extends RequestCallback {
		public void onSuccess(List<BookEx> booklist);
	}
	
	//http://hostname/ shelf/getbooklist? cpcode=yzsc &rpid=10
    /*
	名称   	类型	是否必填	描述
	cpcode	string	是	cp
	rpid	string	是	cp的cp
	uid	    string	是	用户编号，cookie中获取
    */ 
	
	public static void GetBookShelfList( final GetBookShelfListCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_BOOKSHELF_LIST;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOK_SHELF_BOOKLIST;
			}

			@Override
		    public void onFail(ErrorResult result)
		    {
		    	
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
					cb.onSuccess((List<BookEx>) object);
					
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
