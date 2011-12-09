package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class NetBookMark {

	
	public String bookmarkid;	//书签编号		
	public String textdesc;	    //说明		
	public String chapteridNet;	//章节编号		
	public String pos;	        //位置		客户端自己定义吧，服务器只需要记录一个位置信息即可，比如1,3,4000 代表第一章，第三页，每页400字的这么一个位置信息。
	public String updatets;	    //最后更新时间	
	public int isDel;

	
	
	public static interface AddNetBookMarkCallback extends RequestCallback {
		public void onSuccess(String strMsg);
	}
	
	//http://hostname/bookmark/add?bookid=1727516&cpcode=yzsc&chapterid=100&pos=40textdesc=&markpicid =101&rpid=10
    /*
	名称	                      类型	是否必填	描述
	cpcode	    string	是	cp
	rpid	    string	是	cp的cp
	bookid	    string	是	书id
	chapterid	string	是	章节
	pos	        string	是	位置
	textdesc	string	是	书签说明
	markpicid	string	是	书签图片id
	uid	        string	是	用户编号，cookie中获取
    */
	public static void Add(final String cpcode,final String rpid,final String bookid,final String chapterid,final String pos,
			final String textdesc,final String markpicid,final AddNetBookMarkCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_ADD_BOOKMARK;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOKMARK_ADD;
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
				paramsHashMap.put("chapterid", chapterid);
				paramsHashMap.put("pos", pos);
				paramsHashMap.put("textdesc", textdesc);
				paramsHashMap.put("markpicid", markpicid);
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
	
	public static interface DeleteNetBookMarkCallback extends RequestCallback {
		public void onSuccess(String strMsg);
	}
	//http://hostname/bookmark/delete? bookmarkid =101
    /*
		名称	类型	是否必填	描述
		bookmarkid	string	是	书签id
		uid	string	是	用户编号，cookie中获取
	*/
	public static void Delete(final String bookmarkid,final DeleteNetBookMarkCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_ERROR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOKMARK_DELETE;
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
				paramsHashMap.put("bookmarkid",bookmarkid);
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
	
	public static interface GetNetBookMarkListCallback extends RequestCallback {
		public void onSuccess(List<NetBookMark> bookmarklist);
	}
	//http://hostname/ bookmark/getlist? bookid=1727516&cpcode=yzsc&rpid=10
    /*
		名称	类型	是否必填	描述
		cpcode	string	是	cp
		rpid	string	是	cp的cp
		bookid	string	是	书id
    */
	
	public static void GetBookMarkList(final String cpcode,final String rpid,final String bookid,final GetNetBookMarkListCallback cb)
	{
		final String url = "";
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_GET_BOOKMARK_LIST;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_BOOKMARK_LIST;
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
					cb.onSuccess((List<NetBookMark>) object);
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


}
