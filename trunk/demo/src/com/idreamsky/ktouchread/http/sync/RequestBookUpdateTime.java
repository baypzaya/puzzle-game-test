package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.xmlparase.GetBookUpdateTimeParase;
import com.idreamsky.ktouchread.xmlparase.ServerErrorParser;

public class RequestBookUpdateTime  extends RequestNetWork{
	public RequestBookUpdateTime()
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
		return UrlUtil.URL_GET_BOOK_UPDATE_TIME;
	}

	@Override
	protected int getDateType() {
		// TODO Auto-generated method stub
		return DATA_TYPE_STRING;
	}

	@Override
	protected HashMap<String, String> GetParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return true;
	}
    public boolean GetUpdate()
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
		final int code = GetResponseCode();

		if (code == 200) {
			boolean bUpdateBook = false;
			boolean bUpdateBookmark = false;
			ErrorResult result= (ErrorResult)new ServerErrorParser(GetResponse()).parse();
			LogEx.Log_V("APIMSG", "ResultCode:" + Integer.toString(result.ResultCode) +" ResultMsg:" + result.ResultMsg);
        	if(result.ResultCode == UrlUtil.Success)
        	{
        		String timeString  = (String)new GetBookUpdateTimeParase(GetResponse()).parse();
        		String[] time = timeString.split(";");
        	    if(UrlUtil.BookUpdateTime == null || UrlUtil.BookUpdateTime.length() < 1)
        	    {
        	    	bUpdateBook = true;
        	    	if(time[0].compareTo("null") == 0)
        	    	{
        	    		bUpdateBook = true;
        	    	}
        	    	else {
        	    		UrlUtil.BookUpdateTime = time[0];


					}
        	    }else if(time[0].compareTo("null") != 0){
        	    	
					if(isDateBefore(UrlUtil.BookUpdateTime,time[0]))
						 bUpdateBook = true;
				
				}
        	    
        	    
        	    if(UrlUtil.BookMarkUpdateTime == null || UrlUtil.BookMarkUpdateTime.length() < 1)
        	    {
        	    	bUpdateBookmark = true;
        	    	if(time[1].compareTo("null") == 0)
        	    	{
        	    		bUpdateBookmark = true;
        	    	}
        	    	else {
        	    		UrlUtil.BookMarkUpdateTime = time[1];
					}
        	    }else if(time[0].compareTo("null") != 0){
					if(isDateBefore(UrlUtil.BookMarkUpdateTime,time[1]))
						bUpdateBookmark = true;
				}
        	    
        	    
        		return bUpdateBook || bUpdateBookmark;
        	}
		}
		LogEx.Log_V("RequestURL", mUrl);
		return false;
    }
    
    public boolean isDateBefore(String date1,String date2){
    	  try{
    	   DateFormat df = DateFormat.getDateTimeInstance();
    	   return df.parse(date1).before(df.parse(date2));
    	  }catch(ParseException e){
    	   System.out.print("[SYS] " + e.getMessage());
    	   return false;
    	  }
    	} 
}
