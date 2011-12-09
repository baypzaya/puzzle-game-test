package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;


public class NetBook {
	public static int TotalCount = 0;
	public String cpcode;        //CP编号
	public String rpid;          //CP的CP编号
	public String bookid;        //书籍编号
	public String bookname;      //书名
	public String authorname;    //作者名
	public String coverimageurl; //封面图片路径

	public static interface GetNetBookListCallback extends RequestCallback {
		public void onSuccess(List<NetBook> bookList);
	}
	
	//http://hostname/category/getbooklist?catid=2293&level=1&pageindex=1&pagesize=100&cpcode=yzsc
    /*
	名称	       类型	    是否必填	   描述
	catid	   string	是	     分类id
	level	   string	是	     分类层级
	pageindex  string	是	             页码
	pagesize   string	是	     页大小
	cpcode	   string	是	     cp代码，
	yzsc：云中书城
	*/
	public static void GetCategoryBookList(final Category category,final int pageIndex,final int pagesize,final GetNetBookListCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_GET_NETBOOK_LIST;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_CATEGORY_BOOKLIST;
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
					cb.onSuccess((List<NetBook>) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("catid", category.categoryid);
				paramsHashMap.put("level", Integer.toString(category.level));
				paramsHashMap.put("pageindex", Integer.toString(pageIndex));
				paramsHashMap.put("pagesize", Integer.toString(pagesize));
				paramsHashMap.put("cpcode", category.cpcode);
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
	//http://hostname/ toplist /getbooklist ? toplistid=51&cpcode=yzsc
	/*
	名称      	类型	    是否必填	           描述
	toplistid	string	是	        排行榜id
	cpcode	    string	是	        cp编号
	云中书城：yzsc
	*/

	public static void GetTopBookList(final Top top,final GetNetBookListCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_GET_NETBOOK_LIST;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_TOP_BOOKLIST;
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
					cb.onSuccess((List<NetBook>) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("toplistid", top.toplistid);
				paramsHashMap.put("cpcode", top.cpcode);
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
	
	public static void GetRecommenBookList(final GetNetBookListCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_GET_NETBOOK_LIST;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_RECOMMEND;
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
					cb.onSuccess((List<NetBook>) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("cpcode", "yzsc");
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
	
	//http://hostname/search/searchbook?cpcode=yzsc&rpid=&billingtype=&catid=2292&level=3&key=&scope=default&length=&bookstatus=& &pageindex=1&pagesize=1
/*
	名称	         类型	  是否必填	   描述
	key	         string	  是	         关键字
	cpcode	     string	  否	         cp编号
	yzsc：云中书城
	rpid	     string	  否	         cp的cp
	billingtype	 string	  否	                        支付类型  1：整本 2：章节 3：免费
	level	     string	     否	                        分类层级
	catid	     string	     否	                       分类id
	scope	     string	     否	                       搜索类型
	length	     string	     否	                      篇幅
	bookstatus	 string	  否      	作品状态 1连载中 0完结
	pageindex	 string	  否	        页码
	pagesize	 string	  否	                        页大小
*/
	public static void Search(final String key,final GetNetBookListCallback cb)
	{
		final String url = "";
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_GET_NETBOOK_LIST;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOK_SEARCH;
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
					cb.onSuccess((List<NetBook>) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = new HashMap<String, String>();
				paramsHashMap.put("key", key);
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
