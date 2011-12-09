package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class BookMarkFactory {
	private static boolean LoadAdbertLocal = false;
	private static boolean LoadPICLoacal = false;
	
	public static class BookMarkAdvert
	{
		public String subjectid;	//书签主题编号	string	
		public String subjectname;	//书签主题名称	string	
		public String subjectImgUrl;//	书签主题图片Url	string	
		
		public boolean bUpdateBookmarkpic = true;

	}
	public static class BookMarkPIC
	{
		public String bookmarkid;//	书签编号	string
		public String textdesc;//	说明	string
		public String title	;//标题说明	string
		public String ImgUrl;//	标签图片Url	string
		public int isFree;    // 0:免费;1:付费
		public String  price; //价格

	}
	
	public static interface GetBookMarkAdvertListCallback extends RequestCallback {
		public void onSuccess(List<BookMarkAdvert> listBookMarkAdvert);
	}
	
	public static interface GetBookMarkPicListCallback extends RequestCallback {
		public void onSuccess(List<BookMarkPIC> listBookMarkAdvert);
	}
	
	
	public static void GetBookMarkAdvertList(final GetBookMarkAdvertListCallback cb)
	{
		LoadAdbertLocal = false;
		final List<BookMarkAdvert> advertList = BookDataBase.getInstance().QueryAdvertBookmark();
		if(advertList.size() > 0)
		{
			LoadAdbertLocal = true;
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(cb!= null)
					  cb.onSuccess(advertList);
					super.run();
				}
			}.start();
		}
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_BOOKMARK_ADVERT;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_GET_BOOKMARK_ADVERT;
			}

			@Override
			public void onFail(String msg) {
				if (null != cb) {
					cb.onFail(msg);
				}
			}
			
			@Override
			public void onSuccess(Object object) {
				
				final List<BookMarkAdvert> adverts = (List<BookMarkAdvert> ) object;

				if (null != cb && !LoadAdbertLocal) {
					cb.onSuccess(adverts);
				}
				
				new Thread(){
					public void run() {
						BookDataBase.getInstance().InsertBookMarkAdverts(adverts);
					};
				}.start();
				

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

	public static void GetBookMarkPicList(final String subjectid,final GetBookMarkPicListCallback cb,final boolean bUpdate)
	{
		LoadPICLoacal = false;
		final List<BookMarkPIC> picList = BookDataBase.getInstance().QueryBookMarkPic(subjectid);
		if(picList.size() >0 )
		{
			LoadPICLoacal = true;
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(cb != null)
						cb.onSuccess(picList);
					super.run();
				}
			}.start();
		}
		if(bUpdate)
		{
			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_BOOKMARK_PIC_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_GET_BOOKMARK_LIST;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
					final List<BookMarkPIC> bookMarkPICs = (List<BookMarkPIC> ) object;
					if (null != cb && !LoadPICLoacal) {
						cb.onSuccess(bookMarkPICs);
					}
					new Thread(){
						public void run() {
							BookDataBase.getInstance().InsertBookMarkPIC(bookMarkPICs, subjectid);
						};
					}.start();
				}

				@Override
				public HashMap<String, String> getData() {
					HashMap<String, String> paramsHashMap = new HashMap<String, String>();
					paramsHashMap.put("subjectid",subjectid);
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

}
