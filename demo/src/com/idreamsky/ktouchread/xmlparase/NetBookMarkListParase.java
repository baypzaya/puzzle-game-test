package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.data.net.NetBookMark;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class NetBookMarkListParase extends AbstractParser{

	public NetBookMarkListParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		List<NetBookMark> listNetBookMark = new ArrayList<NetBookMark>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			NetBook.TotalCount = data.getInt("Totalcount");
			JSONArray EntryArray = data.getJSONArray("Entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject bookjson = EntryArray.getJSONObject(i); 
		         NetBookMark bookmark = new NetBookMark();
//		         bookmark. = bookjson.getString("RpId");
//		         bookmark.bookid = bookjson.getString("BookId");
//		         bookmark.bookname = bookjson.getString("CpCode");
		         bookmark.chapteridNet = bookjson.getString("ChapterId");
		         bookmark.bookmarkid = bookjson.getString("BookMarkId");
		         bookmark.textdesc = bookjson.getString("TextDesc");
		         bookmark.pos = bookjson.getString("Pos");
		         bookmark.updatets = bookjson.getString("Updatets");
		         bookmark.isDel = bookjson.getInt("IsDel");
		         listNetBookMark.add(bookmark);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return listNetBookMark;
	}
}