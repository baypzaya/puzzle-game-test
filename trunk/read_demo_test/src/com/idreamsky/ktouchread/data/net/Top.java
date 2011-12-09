package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class Top {
	public static int TotalCount;
	public int TopIdDB;
	public String cpcode;      //CP编号
	public String toplistid;   //排行榜编号
	public String toplistname; //排行榜名称
	public String toplistdesc; //排行榜描述
	
	public Top(int id ,String cpcode,String toplistid,String toplistname,String toplistdesc)
	{
		TopIdDB = id;
		this.cpcode = cpcode;
		this.toplistid = toplistid;
		this.toplistname = toplistname;
		this.toplistdesc = toplistdesc;
	}
	public Top()
	{
		
	}
	public static interface GetTopListCallback extends RequestCallback {
		public void onSuccess(List<Top> topList,boolean bFalseData);
		public void onUpdate(List<Top> topList);
	}
	
	//http://hostname/ toplist/getlist
	public static void GetTopList(final GetTopListCallback cb)
	{
		final List<Top> topList = BookDataBase.getInstance().QueryAllTop();
		if(topList.size() <= 0)
		{
			for (int i = 0 ; i< 8 ;i ++)
			{
				Top top = new Top();
				top.toplistid = "-1";
				topList.add(top);
			}
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					cb.onSuccess(topList,true);
					super.run();
					
				}
			}.run();
			

			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_TOP_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_TOP;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
					List<Top> TopList = (List<Top>) object;
					
					
					if (null != cb) {
						cb.onUpdate(TopList);
					}
					for(int i = 0 ; i < TopList.size() ; i ++)
					{
						Top top = TopList.get(i);
						if(BookDataBase.getInstance().InsertTop(top))
						{
							LogEx.Log_V("TopInsertToDB", "Success");
						}
						else {
							LogEx.Log_V("TopInsertToDB", "Failed");
						}	
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
		else 
		{
			if(cb != null)
			{
				cb.onSuccess(topList,false);
			}
			
			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_TOP_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_TOP;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
					final List<Top> TopList = (List<Top>) object;
					if (null != cb) {
						cb.onUpdate(TopList);
					}
					new Thread(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for(int i = 0 ; i < TopList.size() ; i ++)
							{
								Top top = TopList.get(i);
								if(BookDataBase.getInstance().IsExistTop(top.toplistid))
								{
									if(BookDataBase.getInstance().UpdateTop(top))
									{
										LogEx.Log_V("TopUpdateToDB", "Success");
									}
									else
									{
										LogEx.Log_V("TopUpdateToDB", "Fail");
									}
								}
								else
								{
									if(BookDataBase.getInstance().InsertTop(top))
									{
										LogEx.Log_V("TopInsertToDB", "Success");
									}
									else
									{
										LogEx.Log_V("TopInsertToDB", "Fail");
									}
								}
								
							}
						}
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

	}
}
