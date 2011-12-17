package com.idreamsky.ktouchread.http.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.Base64;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.TokenParase;

public class RequestGetToken extends RequestNetWork {
	public RequestGetToken()
	{
		try {
			Init();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	protected int getDateType() {
		// TODO Auto-generated method stub
		return DATA_TYPE_STRING;
	}
	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
		return UrlUtil.URL_GET_TOKEN;
	}

	@Override
	protected HashMap<String, String> GetParams() {
		// TODO Auto-generated method stub
		HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
		paramsHashMap.put("t", UrlUtil.TokenT);		
		paramsHashMap.put("os", UrlUtil.os);
		paramsHashMap.put("resolution", UrlUtil.resolution);
		return paramsHashMap;
	}

	@Override
	protected boolean getIsUserCookie() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean GetToken()
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
			try {
				String Token = (String) new TokenParase(GetResponse()).parse();
				if(Token != null && Token.length() > 0)
				{
					UrlUtil.TokenTPL = Token;
					byte[] buffer = Base64.decodeBase64(UrlUtil.TokenTPL);
					if(buffer != null && buffer.length > 4)
					{
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append(buffer[0]);
						stringBuffer.append(buffer[1]);
						stringBuffer.append(buffer[2]);
						stringBuffer.append(buffer[3]);
						UrlUtil.USERID  = stringBuffer.toString();
						LogEx.Log_V("Token","User ID :" + stringBuffer.toString());
						
					}
					return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	        
		}
		LogEx.Log_V("RequestURL", mUrl);
		return false;
	}

}
