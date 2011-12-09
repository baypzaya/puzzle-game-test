package com.idreamsky.ktouchread.xmlparase;

import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.BookInfo;
import com.idreamsky.ktouchread.util.LogEx;



public class BookInforParase extends AbstractParser{

	public BookInforParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{			
		BookInfo bookInfo = new BookInfo();
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			
	
			bookInfo.rpid = data.getString("rpid");
			bookInfo.categoryid = data.getString("categoryid");
			bookInfo.bookid = data.getString("bookid");
			bookInfo.bookname = data.getString("bookname");
			bookInfo.authorname = data.getString("authorname");
		    bookInfo.coverimageurl = data.getString("coverimageurl");
		    bookInfo.cpcode = data.getString("cpcode");
		    bookInfo.CategoryName = data.getString("categoryname");
		    String billingtype =data.getString("billingtype");
		    if(billingtype != null && billingtype.length()>0)
		    {
		    	bookInfo.billingtype = Integer.parseInt(billingtype);
		    }
		  //  bookInfo.billingtype = data.getInt("BillingType");
		    bookInfo.price = data.getString("price");
		    bookInfo.description = data.getString("description");
		    String status =data.getString("bookstatus");
		    if(status != null && status.length()>0)
		    {
		    	bookInfo.bookstatus = Integer.parseInt(status);
		    }
		    
		   // bookInfo.bookstatus = data.getInt("BookStatus");
		  //  bookInfo.chaptercount = data.getInt("ChapterCount");
		    
		    String count =data.getString("chaptercount");
		    if(count != null && count.length()>0)
		    {
		    	bookInfo.chaptercount = Integer.parseInt(count);
		    }
		    
		    bookInfo.LastUpdateChapterId = data.getString("lastupdatechapterid");
		    bookInfo.LastUpdateChapterName = data.getString("lastupdatechaptername");
		    bookInfo.LastUpdateChapterTs = data.getString("lastupdatechapterts");
		    bookInfo.Updatets = data.getString("updatets");
		     
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
		}
		return bookInfo;
	}
}