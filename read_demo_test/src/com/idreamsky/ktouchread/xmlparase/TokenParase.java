package com.idreamsky.ktouchread.xmlparase;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class TokenParase extends AbstractParser{
	public TokenParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse() throws IOException, JSONException {
		// TODO Auto-generated method stub
		ErrorResult errorResult = new ErrorResult();
		String Token = null;
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			errorResult.ResultCode = Integer.parseInt(jsonObject.getString("ResultCode"));
			errorResult.ResultMsg = jsonObject.getString("ResultMsg");
			if(errorResult.ResultCode == 200)
				Token = jsonObject.getString("Tpl");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			errorResult.ResultCode = 100;
			errorResult.ResultMsg = "获取数据异常！";
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			} 
		}
		
		return Token;
	}

}
