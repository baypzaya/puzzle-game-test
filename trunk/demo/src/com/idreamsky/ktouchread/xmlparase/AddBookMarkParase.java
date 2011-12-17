package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class AddBookMarkParase extends AbstractParser{
	public AddBookMarkParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{
		String bookMarkIDNet = "";
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			bookMarkIDNet = jsonObject.getString("BookMarkId");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			} 
		}
		
		return bookMarkIDNet;
		
		
	}

}
