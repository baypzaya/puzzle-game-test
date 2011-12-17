package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class NetChapterContent {
	public String cpcode;
	public String rpid;
	public String bookid;
	public String bookname;
	public String ChapterId;
	public String ChapterContent;
	public String ChapterName;
	public String PreChapterId;
	public String NextChapterId;
	public int PreChapterIdIsFree;
	public int NextChapterIdIsFree;
	public int WordCount;
	public int IsFree;
	public String price;
	public String Updatets;
	public String VolumeName;
	
	public static interface GetChapterContentCallback extends RequestCallback {
		public void onSuccess(NetChapterContent chaptercontent);
		public void onFail(ErrorResult result);
	}
		

/*	
	名称	       类型	   是否必填	描述
	cpcode	   string	是	    cp
	rpid	   string	是	    cp的cp
	bookid	   string	是	          书id
	chapterid  string	是	    章节id
*/
	
	public static void  GetChapterContent(final String cpcode,final String rpid,final String bookid,final String chapterid,final GetChapterContentCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_CHAPTER_CONTENT;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_CHAPTER;
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
					cb.onSuccess((NetChapterContent) object);
				}
			}

			
			@Override
			public HashMap<String, String> getData() {
				HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
				paramsHashMap.put("cpcode",cpcode);
				paramsHashMap.put("rpid", rpid);
				paramsHashMap.put("bookid", bookid);
				paramsHashMap.put("chapterid", chapterid);
				
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
