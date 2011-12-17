package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.Order;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class ChapterOrderParase extends AbstractParser{

	public ChapterOrderParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		Order.OrderInfor orderInfor = new Order.OrderInfor();
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(dataSource);
			
			orderInfor.ResultCode = Integer.parseInt(jsonObject.getString("ResultCode"));
			orderInfor.Msg = jsonObject.getString("ResultMsg");
			
			orderInfor.balance = jsonObject.getString("Balance");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		
		return orderInfor;
	}
}