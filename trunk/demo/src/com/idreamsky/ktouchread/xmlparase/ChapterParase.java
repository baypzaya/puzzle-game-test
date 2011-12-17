package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;

public class ChapterParase extends AbstractParser{

	public ChapterParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		NetChapter chapter = new NetChapter();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			chapter.cpcode = data.getString("cpcode");
			chapter.bookid = data.getString("bookid");
			chapter.rpid = data.getString("rpid");
			chapter.billingtype = data.getInt("billingtype");
			chapter.totalcount = data.getInt("totalcount");
			chapter.bookStatus = data.getInt("bookstatus");
			
			JSONArray EntryArray = data.getJSONArray("entry");
			
			chapter.mChapterInfoList = new ArrayList<NetChapter.ChapterInfo>();
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		    	 NetChapter.ChapterInfo chapterInfo = new NetChapter.ChapterInfo();
		    	 
		         JSONObject categoryjson = EntryArray.getJSONObject(i); 
		         chapterInfo.chapterid = categoryjson.getString("chapterid");
		         chapterInfo.chaptername = categoryjson.getString("chaptername");
		         chapterInfo.VolumeName = categoryjson.getString("volumename");
		         chapterInfo.wordcount = categoryjson.getInt("wordcount");
		         chapterInfo.isfree = categoryjson.getInt("isfree");
		         chapterInfo.price = categoryjson.getString("price");
		         chapterInfo.updatets = categoryjson.getString("updatets");

		         chapter.mChapterInfoList.add(chapterInfo);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		LogEx.Log_V("APIMSG", "Chapter Count:" + Integer.toString(chapter.mChapterInfoList.size()) );
		return chapter;
	}
}