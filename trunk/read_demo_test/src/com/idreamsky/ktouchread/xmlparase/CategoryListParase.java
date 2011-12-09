package com.idreamsky.ktouchread.xmlparase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idreamsky.ktouchread.data.net.Category;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;



public class CategoryListParase extends AbstractParser{

	public CategoryListParase(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object parse() 
	{	
		List<Category> listCategory = new ArrayList<Category>();
		
		
		try {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(dataSource);
			JSONObject data = jsonObject.getJSONObject("Data"); 
			Category.TotalCount = data.getInt("Totalcount");
			JSONArray EntryArray = data.getJSONArray("Entry");
			
		     for ( int i=0;i<EntryArray.length() ;i++)  
		     {  
		         JSONObject categoryjson = EntryArray.getJSONObject(i); 
		         Category category = new Category();
		         category.categoryid = categoryjson.getString("categoryid");
		         category.categoryname = categoryjson.getString("categoryname");
		         category.bookcount = categoryjson.getInt("bookcount");
		         category.level = categoryjson.getInt("level");
		         category.parentid = categoryjson.getString("parentid");
		         category.cpcode = categoryjson.getString("cpcode");

		         listCategory.add(category);
		     }
		     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(Configuration.DEBUG_VERSION)
			{
				LogEx.Log_V("JsonParase", this.getClass().getName() + ":"  + e.toString());
			}
		}
		return listCategory;
	}
}