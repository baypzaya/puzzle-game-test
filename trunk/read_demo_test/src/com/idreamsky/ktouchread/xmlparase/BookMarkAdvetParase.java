package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.BookMarkFactory;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class BookMarkAdvetParase extends AbstractParser{

	public BookMarkAdvetParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse()  {
		// TODO Auto-generated method stub
		
		List<BookMarkFactory.BookMarkAdvert> listBookMarkAdvert = new ArrayList<BookMarkFactory.BookMarkAdvert>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			JSONArray EntryArray = data.getJSONArray("Entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject json = EntryArray.getJSONObject(i); 
		         BookMarkFactory.BookMarkAdvert bookMarkAdvert = new BookMarkFactory.BookMarkAdvert();
		         bookMarkAdvert.subjectid = json.getString("subjectId");
		         bookMarkAdvert.subjectImgUrl = json.getString("subjectImgUrl");
		         bookMarkAdvert.subjectname = json.getString("subjectName");
		         listBookMarkAdvert.add(bookMarkAdvert);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return listBookMarkAdvert;
	}

}
