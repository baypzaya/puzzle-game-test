package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.AddBookMarkParase;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.ServerErrorParser;


public class RequestAddBookMark extends RequestNetWork {

	public String cpcode; 
	public String rpid;
	public String bookid; 
	public String chapterid; 
	public String pos;
	public String textdesc; 
	public String markpicid;
	
	public RequestAddBookMark(final String cpcode, final String rpid,
			final String bookid, final String chapterid, final String pos,
			final String textdesc, final String markpicid) {
		this.cpcode = cpcode;
		this.rpid = rpid;
		this.chapterid = chapterid;
		this.pos = pos;
		this.textdesc = textdesc;
		this.markpicid = markpicid;
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
		return UrlUtil.URL_BOOKMARK_ADD;
	}

	@Override
	protected HashMap<String, String> GetParams() {
		// TODO Auto-generated method stub
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("cpcode",cpcode);
		paramsHashMap.put("rpid", rpid);
		paramsHashMap.put("bookid",bookid);
		paramsHashMap.put("chapterid", chapterid);
		paramsHashMap.put("pos", pos);
		paramsHashMap.put("textdesc", textdesc);
		paramsHashMap.put("markpicid", markpicid);
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

	public String Add() {
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
		final int code = GetResponseCode();

		if (code == 200) {
			ErrorResult result= (ErrorResult)new ServerErrorParser(GetResponse()).parse();
			LogEx.Log_V("APIMSG", "ResultCode:" + Integer.toString(result.ResultCode) +" ResultMsg:" + result.ResultMsg);	
			if(result.ResultCode == UrlUtil.Success)
			{
				String resultID = (String) new AddBookMarkParase(
						GetResponse()).parse();
		        return resultID;
			}

		}
		LogEx.Log_V("RequestURL", mUrl);
		return null;
	}

	@Override
	protected int getDateType() {
		// TODO Auto-generated method stub
		return DATA_TYPE_STRING;
	}

}
