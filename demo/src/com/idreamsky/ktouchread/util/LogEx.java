package com.idreamsky.ktouchread.util;
import android.util.Log;

public class LogEx {

	static public void Log_V(String tag,String info)
	{
		if(Configuration.DEBUG_VERSION && tag!=null && info!=null)
		{
			Log.v(tag, info);
		}
	}
	
	static public void Log_I(String tag,String info)
	{
		if(Configuration.DEBUG_VERSION && tag!=null && info!=null )
		{
			Log.i(tag, info);
		}
	}
	
	static public void Log_D(String tag,String info)
	{
		if(Configuration.DEBUG_VERSION && tag!=null && info!=null)
		{
			Log.d(tag, info);
		}
	}
}
