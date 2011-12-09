package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ServerErrorParser;

public class RequestAddBookshelf  extends RequestNetWork {

	private String cpcode;
	private String rpid;
	private String bookid;
	public RequestAddBookshelf(final String cpcode,final String rpid,final String bookid)
	{
		super();
		this.cpcode = cpcode;
		this.rpid = rpid;
		this.bookid = bookid;
		try {
			Init();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return UrlUtil.URL_BOOK_SHELF_ADD;
	}

	@Override
	protected int getDateType() {
		// TODO Auto-generated method stub
		return DATA_TYPE_STRING;
	}
	@Override
	protected HashMap<String, String> GetParams() {
		// TODO Auto-generated method stub
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
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return true;
	}
	public boolean Add()
	{
		try {
			this.excute();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		final  int code = GetResponseCode();

        if(code == 200)
        {
        	ErrorResult result= (ErrorResult)new ServerErrorParser(GetResponse()).parse();
        	LogEx.Log_V("APIMSG", "ResultCode:" + Integer.toString(result.ResultCode) +" ResultMsg:" + result.ResultMsg);
        	if(result.ResultCode == UrlUtil.Success)
        	{
        		return true;
        	}
        }
        return false;
	}

}
