package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.idreamsky.ktouchread.data.net.NetBookMark;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.NetBookMarkListParase;
import com.idreamsky.ktouchread.xmlparase.ServerErrorParser;

public class RequestGetBookMark extends RequestNetWork{
	private String cpcode;
	private String rpid;
	private String bookid;
	public RequestGetBookMark(final String cpcode,final String rpid,final String bookid)
	{
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
		return UrlUtil.URL_BOOKMARK_USER_LIST;
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
		paramsHashMap.put("cpcode",cpcode);
		paramsHashMap.put("rpid", rpid);
		paramsHashMap.put("bookid",bookid);
		paramsHashMap.put("os", UrlUtil.os);
		paramsHashMap.put("resolution", UrlUtil.resolution);
		return paramsHashMap;
	}

	@Override
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return true;
	}
	public List<NetBookMark> GetBookMark()
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
				return (List<NetBookMark>)new NetBookMarkListParase(GetResponse()).parse();
			}
			
		}
		return null;
	}

}
