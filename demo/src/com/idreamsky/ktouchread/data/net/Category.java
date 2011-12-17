package com.idreamsky.ktouchread.data.net;

import java.util.HashMap;
import java.util.List;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.JsonRequest;
import com.idreamsky.ktouchread.http.RequestCallback;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.xmlparase.ParserFactory;
public class Category {
	public static int TotalCount = 0;
	
	public int CategoryIdDB;
	public String cpcode;        //CP编号
	public String categoryid;    //分类编号
	public String categoryname;  //分类名称
	public String parentid;      //父类别编号
	public int level;            //当前分类层级
	public int bookcount;        //当前分类书籍数量
	
	public Category(int id,String cpcode,String categoryid,String categoryname,String parentid,int level,int bookcount)
	{
		CategoryIdDB = id;
		this.cpcode = cpcode;
		this.categoryid = categoryid;
		this.categoryname = categoryname;
		this.parentid = parentid;
		this.level = level;
		this.bookcount = bookcount;
	}
	public Category()
	{
		
	}
	public static interface GetCategoryListCallback extends RequestCallback {
		public void onSuccess(List<Category> categorylist,boolean bFalseData);
		public void onUpdate(List<Category> categorylist);
	}
	
	//http://hostname/category/getlist
	public static void GetCategoryList(final GetCategoryListCallback cb)
	{
		final List<Category> categoryList = BookDataBase.getInstance().QueryAllCategory();
		if(categoryList.size() <= 0)
		{
			for(int i = 0 ; i < 8 ;i++)
			{
				Category category = new Category();
				category.categoryid = "-1";
				categoryList.add(category);
				
			}
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					cb.onSuccess(categoryList,true);
					super.run();
					
				}
			}.run();
			
			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_CATEGORY_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_CATEGORY;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
					List<Category> CategoryList = (List<Category>) object;
					if (null != cb) {
						cb.onUpdate(CategoryList);
					}
					BookDataBase.getInstance().InsertCategory(CategoryList);
//					for(int i = 0 ; i < CategoryList.size() ; i ++)
//					{
//						Category category = CategoryList.get(i);
//						if(BookDataBase.getInstance().InsertCategory(category))
//						{
//							LogEx.Log_V("CategroyInsertToDB", "Success");
//						}
//						else {
//							LogEx.Log_V("CategroyInsertToDB", "Failed");
//						}	
//					}
				}

				@Override
				public HashMap<String, String> getData() {
					HashMap<String, String> paramsHashMap = new HashMap<String, String>();
					paramsHashMap.put("imei", UrlUtil.imei);
					paramsHashMap.put("imsi", UrlUtil.imsi);
					paramsHashMap.put("deviveid", UrlUtil.deviveid);
					paramsHashMap.put("os", UrlUtil.os);
					paramsHashMap.put("resolution", UrlUtil.resolution);
					return paramsHashMap;
				}

				@Override
				public boolean getIsUserCookie() {
					// TODO Auto-generated method stub
					return true;
				}
			}.makeRequest();
		}
		else {
			if(cb != null)
			{
				cb.onSuccess(categoryList,false);
			}
			
			new JsonRequest() {

				@Override
				public int getParserType() {
					return ParserFactory.TYPE_CATEGORY_LIST;
				}

				@Override
				public String getPath() {
					return UrlUtil.URL_CATEGORY;
				}

				@Override
				public void onFail(String msg) {
					if (null != cb) {
						cb.onFail(msg);
					}
				}
				
				@Override
				public void onSuccess(Object object) {
					final List<Category> CategoryList = (List<Category>) object;
					if (null != cb) {
						cb.onUpdate(CategoryList);
					}
					new Thread(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for(int i = 0 ; i < CategoryList.size() ; i ++)
							{
								Category category = CategoryList.get(i);
								if(BookDataBase.getInstance().IsExistCategory(category.categoryid))
								{
									if(BookDataBase.getInstance().UpdateCategory(category))
									{
										LogEx.Log_V("CategroyUpdateToDB", "Success");
									}
									else
									{
										LogEx.Log_V("CategroyUpdateToDB", "Fail");
									}
								}
								else
								{
									if(BookDataBase.getInstance().InsertCategory(category))
									{
										LogEx.Log_V("CategroyInsertToDB", "Success");
									}
									else
									{
										LogEx.Log_V("CategroyInsertToDB", "Fail");
									}
								}
								
							}
						}
					}.start();
					

				}

				@Override
				public HashMap<String, String> getData() {
					HashMap<String, String> paramsHashMap = UrlUtil.getAppInfoData();
					paramsHashMap.put("os", UrlUtil.os);
					paramsHashMap.put("resolution", UrlUtil.resolution);
					return paramsHashMap;
				}

				@Override
				public boolean getIsUserCookie() {
					// TODO Auto-generated method stub
					return true;
				}

			}.makeRequest();
			
		}

	}	
}
