
package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.bookread.BookReadActivity;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;


public class PriceParase extends AbstractParser{

	public PriceParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			BookReadActivity.Price = jsonObject.getString("Price");
			BookReadActivity.PayInRedPacket = jsonObject.getString("PayInRedPacket");
			BookReadActivity.PayInCash = jsonObject.getString("PayInCash");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			} 
		}
		
		return null;
	}
}