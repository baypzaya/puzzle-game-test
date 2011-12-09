package com.idreamsky.ktouchread.data.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.bookshelf.Poster;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;


public class Advert implements Serializable{
	
	private static final long serialVersionUID = 2920190535200304447L;
	public int SeqNo	;//排序号
	public String ImgUrl ;//	图片地址
	public String bookid ;//	书籍编号
	public String bookname;//	书名
	public String rpid	;//CP的CP
	public String cpcode ;//	CP
	public String title	;//描述
	public String updatets	;//更新时间
	public String Id;
	public int Pos;
	public int Status;
	
	public static int TotalCount = 0;
	
	

	//http://hostname/advert/getlist?pos=1
    /*
		名称	类型	是否必填	描述
		pos	string	是	广告位置1：我的书架2：在线书城3：书签
		*/

	public static interface GetAdvertListCallback extends RequestCallback {
		public void onSuccess(List<Advert> listAdvert);
	}
	
	public static void GetAdvertList(final String pos ,final GetAdvertListCallback cb)
	{
		List<Advert> advertList = null;
		if(pos.compareTo(Poster.myBookShelf) == 0)
		{
			advertList = BookDataBase.getInstance().QueryAdvert(1);
		}else if(pos.compareTo(Poster.bookShop) == 0)
		{
			advertList = BookDataBase.getInstance().QueryAdvert(2);
		}
		
		if(advertList.size() >0)
		{
			cb.onSuccess(advertList);
			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_ADVERT_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_ADVERT_LIST;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
//					if (null != cb) {
//						cb.onSuccess((List<Advert> ) object);
//					}
					
					if(pos.compareTo(Poster.myBookShelf) == 0)
					{
						BookDataBase.getInstance().InsertAdverts((List<Advert> ) object, 1);
					}else if(pos.compareTo(Poster.bookShop) == 0)
					{
						BookDataBase.getInstance().InsertAdverts((List<Advert> ) object, 2);
					}
				}

				@Override
				public HashMap<String, String> getData() {
					HashMap<String, String> paramsHashMap = new HashMap<String, String>();
					paramsHashMap.put("pos", pos);
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
		else
		{
			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_ADVERT_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_ADVERT_LIST;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
					final List<Advert> advertList = (List<Advert> ) object;
					new Thread(){
						public void run() {
							if(pos.compareTo(Poster.myBookShelf) == 0)
							{
								BookDataBase.getInstance().InsertAdverts(advertList, 1);
							}else if(pos.compareTo(Poster.bookShop) == 0)
							{
								BookDataBase.getInstance().InsertAdverts(advertList, 2);
							}
						};
					}.start();
					if (null != cb) {
						cb.onSuccess((List<Advert> ) object);
					}
				}

				@Override
				public HashMap<String, String> getData() {
					HashMap<String, String> paramsHashMap = new HashMap<String, String>();
					paramsHashMap.put("pos", pos);
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
					return false;
				}
			}.makeRequest();
		}
		

	}


}
