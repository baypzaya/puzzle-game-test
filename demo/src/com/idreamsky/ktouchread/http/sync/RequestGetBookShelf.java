package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.idreamsky.ktouchread.data.net.NetBookShelf.BookEx;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.BookShelfListParase;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ServerErrorParser;


public class RequestGetBookShelf extends RequestNetWork{
	
	public RequestGetBookShelf()
	{
		try {
			Init();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return UrlUtil.URL_BOOK_SHELF_BOOKLIST;
	}
	@Override
	protected int getDateType() {
		// TODO Auto-generated method stub
		return DATA_TYPE_STRING;
	}
	@Override
	protected HashMap<String, String> GetParams() {
		// TODO Auto-generated method stub
		HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
		paramsHashMap.put("os", UrlUtil.os);
		paramsHashMap.put("resolution", UrlUtil.resolution);
		paramsHashMap.put("pagesize", "99999");
		return paramsHashMap;
	}

	@Override
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return true;
	}
	public List<BookEx> GetBookShelf()
	{
		try {
			this.excute();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		final  int code = GetResponseCode();
		if(code == 200)
		{
			ErrorResult errorResult = (ErrorResult)new ServerErrorParser(GetResponse()).parse();
			LogEx.Log_V("APIMSG", "ResultCode:" + Integer.toString(errorResult.ResultCode) +" ResultMsg:" + errorResult.ResultMsg);
			if(errorResult.ResultCode == UrlUtil.Success)
			{
				return (List<BookEx>)new BookShelfListParase(GetResponse()).parse();
			}
			
		}
		return null;
	}

}
