package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.BookMarkFactory;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class BookMarkPicListParase extends AbstractParser{

	public BookMarkPicListParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse()  {
		// TODO Auto-generated method stub
		List<BookMarkFactory.BookMarkPIC> listBookMarkPic = new ArrayList<BookMarkFactory.BookMarkPIC>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			JSONArray EntryArray = data.getJSONArray("Entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject json = EntryArray.getJSONObject(i); 
		         BookMarkFactory.BookMarkPIC bookPic = new BookMarkFactory.BookMarkPIC();
		         bookPic.bookmarkid = json.getString("BookMarkId");
		         bookPic.ImgUrl = json.getString("ImgUrl");
		         bookPic.textdesc = json.getString("TextDesc");
		         bookPic.title = json.getString("Title");
		         bookPic.isFree = json.getInt("isFree");
		         bookPic.price = json.getString("price");
		         listBookMarkPic.add(bookPic);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return listBookMarkPic;
	}

}
