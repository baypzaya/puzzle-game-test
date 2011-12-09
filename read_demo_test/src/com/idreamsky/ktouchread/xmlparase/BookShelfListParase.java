package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.data.net.NetBookShelf;
import com.idreamsky.ktouchread.data.net.NetBookShelf.BookEx;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class BookShelfListParase extends AbstractParser{

	public BookShelfListParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		List<BookEx> listNetBook = new ArrayList<BookEx>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			NetBook.TotalCount = data.getInt("Totalcount");
			JSONArray EntryArray = data.getJSONArray("Entry");
			int iii = EntryArray.length();
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject bookjson = EntryArray.getJSONObject(i); 
		         NetBookShelf.BookEx book = new NetBookShelf.BookEx();
		         book.ischase = bookjson.getInt("IsChase");
		         book.chasetype = bookjson.getInt("ChaseType");
		         book.bookstatus = bookjson.getInt("BookStatus");
		        // book.feeds = bookjson.getInt("Feeds");
		         book.rpid = bookjson.getString("rpid");
		         //book.c = bookjson.getString("categoryid");
		         book.bookid = bookjson.getString("bookid");
		         book.bookname = bookjson.getString("bookname");
		         book.authorName = bookjson.getString("authorname");
		         book.coverimageurl = bookjson.getString("coverimageurl");
		         book.cpcode = bookjson.getString("cpcode");
		         book.isDel = bookjson.getInt("IsDel");

		         
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
