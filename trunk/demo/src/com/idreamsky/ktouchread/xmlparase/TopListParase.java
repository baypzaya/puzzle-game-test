package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.Top;
import com.idreamsky.ktouchread.util.LogEx;


public class TopListParase extends AbstractParser{

	public TopListParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		List<Top> listTop = new ArrayList<Top>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			Top.TotalCount = data.getInt("totalcount");
			JSONArray EntryArray = data.getJSONArray("entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject topjson = EntryArray.getJSONObject(i); 
		         Top top = new Top();
		         top.cpcode = topjson.getString("cpcode");
		         top.toplistid = topjson.getString("toplistid");
		         top.toplistname = topjson.getString("toplistname");
		         top.toplistdesc = topjson.getString("toplistdesc");
		         listTop.add(top);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
		}
		return listTop;
	}
}