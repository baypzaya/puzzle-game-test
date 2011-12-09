package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;

import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;

public class Chase {

	public static interface ChaseCallback extends RequestCallback {
		public void onSuccess(String strMsg);
	}
	//http://hostname/chase/add? bookid=1727516&chasetype=1&rpid=10&cpcode=yzsc
    /*
		名称	类型	是否必填	描述
		cpcode	string	是	cp
		rpid	string	是	cp的cp
		bookid	string	是	书id
		uid	    string	是	用户编号，cookie中获取
		chasetype	string	否	追更类型 0：普通1：高级
    */

	public static void Add(final String cpcode,final String rpid,final String bookid,final String chasetype,final ChaseCallback cb)
	{
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_ERROR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_CHASE_ADD;
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
				paramsHashMap.put("os", UrlUtil.os);
				paramsHashMap.put("resolution", UrlUtil.resolution);
				paramsHashMap.put("imei", UrlUtil.imei);
				paramsHashMap.put("imsi", UrlUtil.imsi);
				paramsHashMap.put("deviceIdentify", UrlUtil.deviveid);
				if(chasetype != null)
				{
					paramsHashMap.put("chasetype", chasetype);
				}
				
				return paramsHashMap;
			}

			@Override
			public boolean getIsUserCookie() {
				// TODO Auto-generated method stub
				return true;
			}
		}.makeRequest();
	}
	
		
	//http://hostname/chase/delete? bookid=1727516&uid=1248244765&rpid=10&cpcode=yzsc
    /*
		名称	类型	是否必填	描述
		cpcode	string	是	cp
		rpid	string	是	cp的cp
		bookid	string	是	书id
		uid	string	是	用户编号，cookie中获取
     */
    public static void Delete(final String cpcode,final String rpid,final String bookid,final ChaseCallback cb)
    {
		new JsonRequest() {

			@Override
			public int getParserType() {
				return ParserFactory.TYPE_ERROR;
			}

			@Override
			public String getPath() {
				return UrlUtil.URL_CHASE_DELETE;
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
