package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.ChapterParase;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ServerErrorParser;

public class RequestChaseChapter extends RequestNetWork{
	
	private String rpid;
	private String cpcode;
	private String bookid;
	
	public RequestChaseChapter(final String rpid,final String cpcode,final String bookid)
	{
		this.bookid = bookid;
		this.rpid = rpid;
		this.cpcode = cpcode;
		try {
			Init();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return UrlUtil.URL_GET_UPDATE_CHAPTER;
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
		paramsHashMap.put("bookid", bookid);
		String update = BookDataBase.getInstance().GetNewestChapterUpdateTime(bookid);
		if(update == null || update.length() <2)
		{
			paramsHashMap.put("lastts","1900-01-01 09:04:29" );
		}
		else {
			paramsHashMap.put("lastts",update );
		}		
		paramsHashMap.put("os", UrlUtil.os);
		paramsHashMap.put("resolution", UrlUtil.resolution);
		return paramsHashMap;
	}

	@Override
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return true;
	}

	public NetChapter GetChapter()
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
				return (NetChapter)new ChapterParase(GetResponse()).parse();
			}
			
		}
		return null;
	}
}
