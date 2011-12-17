package com.idreamsky.ktouchread.xmlparase;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;





public class AdvertParase extends AbstractParser{

	public AdvertParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{	
		List<Advert> advertList = new ArrayList<Advert>();
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			Advert.TotalCount = data.getInt("Totalcount");
			JSONArray EntryArray = data.getJSONArray("Entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject advertjson = EntryArray.getJSONObject(i); 
		         Advert advert = new Advert();
		         advert.Id = advertjson.getString("Id");
		         advert.cpcode = advertjson.getString("CpCode");
		         advert.bookid = advertjson.getString("BookId");
		         advert.rpid = advertjson.getString("RpId");
		         advert.SeqNo = advertjson.getInt("SeqNo");
		         advert.Pos = advertjson.getInt("Pos");
		         advert.ImgUrl = advertjson.getString("ImgUrl");
		         advert.title = advertjson.getString("title");
		         advert.updatets = advertjson.getString("Updatets");
                 advert.Status = advertjson.getInt("Status");
		         advertList.add(advert);
		     }
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		
		return advertList;
	}
}