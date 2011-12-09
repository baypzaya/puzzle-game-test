package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;



public class ServerErrorParser extends AbstractParser{

	public ServerErrorParser(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{
		ErrorResult errorResult = new ErrorResult();
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			errorResult.ResultCode = Integer.parseInt(jsonObject.getString("ResultCode"));
			errorResult.ResultMsg = jsonObject.getString("ResultMsg");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			errorResult.ResultCode = 100;
			errorResult.ResultMsg = "获取数据异常！";
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			} 
		}
		
		return errorResult;
	}
}