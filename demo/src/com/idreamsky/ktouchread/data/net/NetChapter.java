package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class NetChapter {
	
	public int       totalcount;	//记录总数
	public String    bookid	;       //书号
	public String    cpcode;	    //cp
	public String    rpid;          //cp的cp
	public int billingtype;
	public int bookStatus; // 1连载中 0完结

	public static class ChapterInfo
	{
	     public String chapterid;      //章节编号
	     public String chaptername;    //章节名称
	     public int wordcount;         //章节字数
	     public int isfree;            //是否免费
	     public String price;           //价格
	     public String updatets;       //最后更新时间
	     public String VolumeName;
	}

	public List<ChapterInfo>  mChapterInfoList;
	
	
	public static interface GetBookChapterCallback extends RequestCallback {
		public void onSuccess(NetChapter NetChapterInfo);
	}
	
	
	//http://hostname/ book/getallchapterinfo? cpcode=yzsc&bookid=5069028&rpid=10
	/*
	名称	    类型	    是否必填	描述
	rpid	string	是	    cp的cp编码
	cpcode	string	是	    cp编码
	bookid	string	是	          书id
	*/

		
     public static void GetBookChapter(final String rpid,final String cpcode,final String bookid ,final GetBookChapterCallback cb)
     {
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_CHAPTER;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_CHAPTER_INFO;
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
					cb.onSuccess((NetChapter) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
				paramsHashMap.put("cpcode",cpcode);
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
     
     
     public static void GetBookChapterUpdate(final String rpid,final String cpcode,final String bookid ,final GetBookChapterCallback cb)
     {
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_CHAPTER;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_UPDATE_CHAPTER;
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
					cb.onSuccess((NetChapter) object);
				}
			}

			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
				paramsHashMap.put("cpcode",cpcode);
				paramsHashMap.put("rpid", rpid);
				paramsHashMap.put("bookid", bookid);
				String update = BookDataBase.getInstance().GetNewestChapterUpdateTime(bookid);
				if(update == null || update.length() <2)
				{
					paramsHashMap.put("lasts","1900-01-01 09:04:29" );
				}
				else {
					paramsHashMap.put("lasts",update );
				}
				
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
