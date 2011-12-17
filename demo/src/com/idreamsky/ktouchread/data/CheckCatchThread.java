package com.idreamsky.ktouchread.data;

import java.io.File;

import android.os.Handler;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.util.SDCardUtils;

public class CheckCatchThread extends Thread{
	public static final int DATA_CATCH_IS_FULL = 101;
	public static interface CheckCatchCallback {
		public void onSuccess();
	}
	private boolean flag = false;
	Handler mHandler;
	public CheckCatchThread(Handler handle)
	{
		mHandler = handle;
	}
	

	public void run()
	{
		long picSize = 0;
		long DbSise = 0;
		String dBPathString = BookDataBase.getInstance().GetDBPath();
		File fileDB = new File(dBPathString);
		DbSise = fileDB.length();
		
		if(DbSise > 52428800)
		{
			mHandler.sendEmptyMessage(DATA_CATCH_IS_FULL);
		}
		
		if(SDCardUtils.isSDCardEnable())
		{
			String pathPic = SDCardUtils.GetpicPath();
			File filePic = new File(pathPic);
			if(filePic.exists())
			{
				File[] files = filePic.listFiles();
				for(int i = 0 ; i < files.length;i++)
				{
					File file = files[i];
					if(file.isFile())
					{
						picSize += file.length();
					}
					
				}
				File[] filesDelete = filePic.listFiles();
				int  index = 0;
				while(picSize > 104857600  && index < filesDelete.length)  //100M
				{
					File file = filesDelete[index];
					if(file.isFile())
					{
						long size = file.length();
					    if(file.delete())
					    {
					    	picSize-=size;
					    }
					}
					
					
					index++;
				}
			}

		}

	
		this.stop();
		flag = false;
		
	}
}
