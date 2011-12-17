package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;



public class GetBookUpdateTimeParase extends AbstractParser{
	public GetBookUpdateTimeParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{
		String TimeBook = "null";
		String TimeBookMark = "null";
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			TimeBook = jsonObject.getString("UpdatetsShelf");
			if(TimeBook == null)
				TimeBook = "null";
			TimeBookMark = jsonObject.getString("UpdatetsBookMark");
			if(TimeBookMark == null)
				TimeBookMark = "null";

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
				TimeBook = "null";
				TimeBookMark = "null";
			} 
		}
		
		return TimeBook +";" +TimeBookMark;
		
		
	}

}
