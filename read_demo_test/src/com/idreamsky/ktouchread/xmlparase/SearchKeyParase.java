package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.bookshop.SearchView;
import com.idreamsky.ktouchread.data.net.SearchKey;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class SearchKeyParase extends AbstractParser{

	public SearchKeyParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		List<String> listSearchKey = new ArrayList<String>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			SearchView.DefaultKey = data.getString("DefaultKey");
			SearchKey.TotalCount = data.getInt("Totalcount");
			JSONArray EntryArray = data.getJSONArray("Entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject bookjson = EntryArray.getJSONObject(i); 
		         
		         String key = bookjson.getString("searchKey");
		         listSearchKey.add(key);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return listSearchKey;
	}
}