package com.txyjssr.share;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class FileCommunicationServer implements Communication{

	@Override
	public void execute(OutputStream out, InputStream in) {
		File savePage = Environment.getExternalStorageDirectory();
		byte[] buffer = new byte[1024];
		int num=0;
		try {			
			while((num=in.read(buffer))>-1){
				String message = new String(buffer,0,num);
				JSONObject jso;
				try {
					jso = new JSONObject(message);
					String fileName = jso.getString("file_name");
					Log.i("yujsh log" , "fileName :"+fileName);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("yujsh log" , "server read end");
	}
	
}
