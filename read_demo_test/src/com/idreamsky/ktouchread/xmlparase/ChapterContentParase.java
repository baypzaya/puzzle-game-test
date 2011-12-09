package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.NetChapterContent;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;


public class ChapterContentParase extends AbstractParser{

	public ChapterContentParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		NetChapterContent chapterContent = new NetChapterContent();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			
	         chapterContent.PreChapterId = data.getString("prechapterid");
	         chapterContent.ChapterName = data.getString("nextchapterid");
	         chapterContent.ChapterContent = data.getString("chaptercontent");
	         chapterContent.ChapterId = data.getString("chapterid");
	         chapterContent.ChapterName = data.getString("chaptername");
	         chapterContent.VolumeName = data.getString("volumename");
	         chapterContent.WordCount = data.getInt("wordcount");
	         chapterContent.IsFree = data.getInt("isfree");
	         chapterContent.price = data.getString("price");
	         
	         
	        // chapterInfo.updatets = data.getString("Updatets");
	         
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return chapterContent;
	}
}