package com.idreamsky.ktouchread.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Util {
	
	public static String UNREAD = "UNREAD"; // 未读
	public static String CHAPTER = "CHAPTER"; // 章节目录跳转过来
	public static String BOOKMARK = "BOOKMARK"; // 书签
	public static String RECENTLYREAD = "RECENTLYREAD"; // 最近阅读
	public static String NOMARLREAD = "NOMARLREAD"; // 最近阅读
	public static String ENTRANCE = "ENTRANCE"; // booRead入口
	public static String ENTRANCEBOOKDETAIL = "ENTRANCEBOOKDETAIL"; // booRead入口
	public static String FONTSIZE = "FONTSIZE"; //字体
	public static String MODEL = "MODEL"; //白天夜间 模式
	public static String MODELSETTING = "modelSetting"; //模式设置
	public static String SIZESETTING = "sizeSetting"; //字体设置
	
	public static boolean isFirstOpenBookShop = false; //是否第一次打开网络书城
	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	
	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	
	public static void toActivity(Activity activity,Class claszz)
	{
		Intent intent = new Intent();
		intent.setClass(activity, claszz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		activity.startActivity(intent);
	}
	
	public static String getCurrentTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}
	public static String getBookMarkDate()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
	
	public static Animation loadAnimation(Context context,int resoureceAnimID)
	{
		Animation animation = AnimationUtils.loadAnimation(context, resoureceAnimID);
		return animation;
	}
	
	public static String writePath = "chapter.txt";
	/**
	  * 写入txt文件
	  * 
	  * @param str
	  *            要写入的内容
	  * @param path
	  *            文件位置
	  * @throws Exception
	  */
	 public static void writer(Activity activity,String str, String writePath) {
	  try {
		  FileOutputStream outStream = activity.openFileOutput(writePath, Context.MODE_PRIVATE);
	         outStream.write(str.getBytes());
	         outStream.close();   
	  } catch (Exception e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	 } 
	//写入文件
		public void readAsFile(InputStream inputStream,File file) throws IOException
		{
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] data = new byte[1024];
			int len =-1;
			while( (len=inputStream.read(data)) !=-1 )
			{
				fileOutputStream.write(data,0,len);		
			}
			fileOutputStream.close();
			inputStream.close();
		}
		
		/**
		 * 保存设置
		 * @param fileName
		 * @param maps
		 */
		public static void saveSharedPreferences(Context context,String fileName,Map<String, String> maps)
		{
			SharedPreferences sharedPreferences  = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			for(String key : maps.keySet())
			{
				editor.putString(key, maps.get(key));
			}
			editor.clear().commit();
		}
		/**
		 * 得到配置
		 * @param fileName
		 * @param s
		 * @return
		 */
		public static Map<String, String> getSharedPreferences(Context context,String fileName,String... s)
		{
			SharedPreferences userInfoPreferences  = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			Map<String, String> maps = new HashMap<String, String>();
			if(userInfoPreferences!=null)
			{
				for(String str:s){
					String obj = userInfoPreferences.getString(str, null);
					maps.put(str, obj);
				}
				return maps;
			}else{
				return null;
			}
			
		}
}
