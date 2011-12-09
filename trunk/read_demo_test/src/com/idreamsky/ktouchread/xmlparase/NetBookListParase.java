package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class NetBookListParase extends AbstractParser{

	public NetBookListParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		List<NetBook> listNetBook = new ArrayList<NetBook>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			NetBook.TotalCount = data.getInt("totalcount");
			JSONArray EntryArray = data.getJSONArray("entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject bookjson = EntryArray.getJSONObject(i); 
		         NetBook book = new NetBook();
		         book.rpid = bookjson.getString("rpid");
		         book.bookid = bookjson.getString("bookid");
		         book.bookname = bookjson.getString("bookname");
		         book.authorname = bookjson.getString("authorname");
		         book.coverimageurl = bookjson.getString("coverimageurl");
		         book.cpcode = bookjson.getString("cpcode");
		         listNetBook.add(book);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return listNetBook;
	}
}